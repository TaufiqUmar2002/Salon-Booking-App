package com.umar.service_offering.service;

import com.umar.events.services.BulkServiceUpdateEvent;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.services.BulkUpdateServiceRequest;
import com.umar.payload.request.services.ai.AiBulkDescribeRequest;
import com.umar.payload.request.services.ai.AiServiceDescribe;
import com.umar.payload.response.category.CategoryResponse;
import com.umar.payload.response.salon.SalonResponseV1;
import com.umar.payload.response.services.ai.*;
import com.umar.service_offering.event.ServiceOfferingEventProducer;
import com.umar.service_offering.exchange.CategoryClient;
import com.umar.service_offering.exchange.SalonClient;
import com.umar.service_offering.model.CategoryPriceBenchmark;
import com.umar.service_offering.model.ServiceOffering;
import com.umar.service_offering.repository.CategoryPriceBenchmarkRepository;
import com.umar.service_offering.repository.ServiceOfferingRepository;
import com.umar.service_offering.serviceinterface.IServiceOfferingAiService;
import com.umar.service_offering.serviceinterface.IServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingAiService implements IServiceOfferingAiService {


    private final ServiceOfferingRepository serviceOfferingRepository;
    private final CategoryPriceBenchmarkRepository categoryPriceBenchmarkRepository;
    private final SalonClient salonClient;
    private final ChatClient chatClient;
    private final CategoryClient categoryClient;
    private final Executor executor;
    private final ServiceOfferingEventProducer eventProducer;
    @Value("classpath:prompts/price-suggest.st")
    private final Resource priceSuggestPrompt;
    @Value("classpath:prompts/service-describe.st")
    private final Resource serviceDescribePrompt;

    @Override
    public AiPriceSuggestResponse priceSuggest(Long serviceId) {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "Service not found",""));
        SalonResponseV1 salonResponse = this.salonClient.getSalonById(serviceOffering.getSalonId());
        CategoryPriceBenchmark categoryPriceBenchmark = categoryPriceBenchmarkRepository.findByCityAndCategoryId(salonResponse.getCity(),serviceOffering.getCategoryId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "Category price benchmark not found",""));
        return chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(
                                priceSuggestPrompt
                ).param("serviceName",serviceOffering.getName())
                        .param("currentPrice",serviceOffering.getPrice())
                        .param("bookingCount",serviceOffering.getBookingCount())
                        .param("cancellationRate",categoryPriceBenchmark.getCancellationRate())
                        .param("avgLeadTime",categoryPriceBenchmark.getAvgLeadTime())
                        .param("demandScore",categoryPriceBenchmark.getDemandScore())
                        .param("competitorAvgPrice",categoryPriceBenchmark.getCompetitorAvgPrice())
                        .param("city",salonResponse.getCity())
                        .param("category",salonResponse.getCategory())).call().entity(AiPriceSuggestResponse.class);
    }

    @Override
    public AiDescribeResponse describe(AiServiceDescribe aiServiceDescribe) {
        ServiceOffering serviceOffering = this.serviceOfferingRepository.findById(aiServiceDescribe.getServiceId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","salon.service.notFound"));
        CategoryResponse categoryResponse = this.categoryClient.getCategoryById(serviceOffering.getCategoryId());
        return chatClient.prompt().
                user(
                        promptUserSpec -> promptUserSpec.text(serviceDescribePrompt)
                                .param("serviceName",serviceOffering.getName())
                                .param("description",serviceOffering.getDescription())
                                .param("category",categoryResponse.getName())
                                .param("keywords",aiServiceDescribe.getKeywords())
                                .param("maxWords",aiServiceDescribe.getMaxWords()))
                .call().entity(AiDescribeResponse.class);
    }

    @Override
    public AiServiceUpsellResponse upsell(Long serviceId) {
        return null;
    }

    @Override
    public AiBulDescribeResponse bulkDescribe(Long salonId, AiBulkDescribeRequest aiBulkDescribeRequest) {
        SalonResponseV1 salonResponse = this.salonClient.getSalonById(salonId);
        List<ServiceOffering> serviceOffering = this.serviceOfferingRepository.findBySalonIdAndDescriptionNotNull(salonId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","salon.service.notFound"));
        if(serviceOffering.isEmpty()){
            throw new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","salon.service.notFound");
        }
        List<AiDescribeResponse> aiDescribeResponses =serviceOffering.stream().map(
                service ->
                        this.describe(
                                AiServiceDescribe.builder().serviceId(service.getId()
                                        ).maxWords(aiBulkDescribeRequest.getMaxWords()).build()
                        )
        ).toList();
        Map<Long,String> serviceIdToDescription = aiDescribeResponses.stream().collect(Collectors.toMap(AiDescribeResponse::getServiceId, AiDescribeResponse::getDescription));
        serviceOffering.forEach(
                service -> {
                    service.setDescription(serviceIdToDescription.get(service.getId()));
                }
        );
        executor.execute(()->{
            this.eventProducer.publishBulkServiceUpdateEvent(
                    BulkServiceUpdateEvent.builder()
                            .affectedServiceIds(serviceOffering.stream().map(ServiceOffering::getId).toList())
                            .changedFields(List.of("description"))
                            .updatedAt(LocalDateTime.now())
                            .build());
        });
        return AiBulDescribeResponse.builder()
                .processedCount(serviceOffering.size())
                .skippedCount(0)
                .build();
    }

    @Override
    public AiServicePerformance performance(Long salonId,Integer periodDays, Integer topN) {
        return null;
    }
}
