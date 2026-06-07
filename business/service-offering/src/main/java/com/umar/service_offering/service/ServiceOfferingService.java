package com.umar.service_offering.service;

import com.umar.events.services.BatchValidationException;
import com.umar.events.services.UpdateServiceEvent;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.services.*;
import com.umar.payload.response.category.LookupCategoryResponse;
import com.umar.payload.response.services.BulkUpdateServiceResponse;
import com.umar.payload.response.services.SearchServiceResponseList;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.service_offering.event.ServiceOfferingEventProducer;
import com.umar.service_offering.exchange.CategoryClient;
import com.umar.service_offering.mapper.ServiceMapper;
import com.umar.service_offering.model.ServiceOffering;
import com.umar.service_offering.model.ServicePriceHistory;
import com.umar.service_offering.repository.ServiceOfferingRepository;
import com.umar.service_offering.repository.ServicePriceHistoryRepository;
import com.umar.service_offering.serviceinterface.IServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingService implements IServiceOfferingService {

    private final ServiceOfferingRepository repository;
    private final CategoryClient categoryClient;
    private final ServiceMapper serviceMapper;
    private final ServicePriceHistoryRepository historyRepository;
    private final ServiceOfferingEventProducer eventProducer;


    @Override
    public ServiceResponse createServiceOffering(CreateServiceRequest request) {
        LookupCategoryResponse lookupCategoryResponse =categoryClient.lookUpCategory(Collections.singletonList(request.getCategoryId()));
        if(!lookupCategoryResponse.getLookupCategoryList().get(0).getIsActive()){
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"CATEGORY_INACTIVE","");
        }
        /* write history and save data and publish kafka event*/
        return null;
    }

    @Override
    public Page<ServiceResponse> getAllServicesBySalonId(Long salonId, ServiceSalonSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(),request.getSize(), Sort.Direction.fromString(request.getSortBy()));
        Page<ServiceOffering> serviceOfferingPage = repository.searchServiceBySalonId(salonId,request.getCategoryId(),pageable);
        return serviceOfferingPage.map(serviceMapper::toResponse);
    }

    @Override
    public ServiceResponse getAllServicesById(Long id) {
        ServiceOffering serviceOffering = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","Service Not found with given id"));
        return serviceMapper.toResponse(serviceOffering);
    }

    @Override
    public ServiceResponse updateService(Long id, UpdateServiceRequest request) {
        ServiceOffering serviceOffering = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","Service Not found with given id"));
        if(request.getRequiresDeposit() && (request.getDepositAmount()==null || request.getDepositAmount().equals(BigDecimal.ZERO))){
            throw new ApiException(HttpStatus.BAD_REQUEST,"DEPOSIT_INCONSISTENT","'depositAmount is required when requiresDeposit is true");
        }
        /* category not found handling*/
        ServiceOffering existingService = this.repository.findServiceOfferingByName(request.getName());
        if(existingService!=null){
            throw new ApiException(HttpStatus.BAD_REQUEST,"DUPLICATE_NAME","A service with this name already exists in your salon");
        }
        if(request.getPrice()!=null && !request.getPrice().equals(serviceOffering.getPrice())){
            ServicePriceHistory priceHistory = ServicePriceHistory.builder()
                    .changedAt(LocalDateTime.now())
                    .changedBy(null)
                    .newPrice(request.getPrice())
                    .oldPrice(serviceOffering.getPrice())
                    .serviceId(id)
                    .build();
            historyRepository.save(priceHistory);
        }
        serviceMapper.UpdateServiceFromRequest(request,serviceOffering);
        repository.save(serviceOffering);
        eventProducer.publishServiceUpdateEvent(new UpdateServiceEvent());
        return serviceMapper.toResponse(serviceOffering);
    }

    @Override
    public void deleteService(Long id, DeleteServiceRequest request) {
        ServiceOffering serviceOffering = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","Service Not found with given id"));
        if(!serviceOffering.getIsActive()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"ALREADY_INACTIVE","This service is already inactive");
        }
        eventProducer.publishServiceDeleteEvent(new DeleteServiceEvent());
        serviceOffering.setIsActive(false);
        repository.save(serviceOffering);
    }

    @Override
    public BulkUpdateServiceResponse bulkUpdateService(BulkUpdateServiceRequest serviceRequest) {
        Map<String, String> errorMap = new HashMap<>();
        if(serviceRequest.getUpdates().size()>50){
            throw new ApiException(HttpStatus.BAD_REQUEST,"BATCH_TOO_LARGE","'Maximum 50 services per bulk update request");
        }
        List<Long> ids  = serviceRequest.getUpdates().stream().map(BulkUpdateServiceRequest.ServiceUpdateItem::getServiceId).toList();
        List<ServiceOffering> existingServices = repository.findAllById(ids);
        Long currentUserId = 77823923L;//get current logged in user
        Map<Long,ServiceOffering> serviceMap = existingServices.stream().collect(Collectors.toMap(ServiceOffering::getId,s->s));
        for(BulkUpdateServiceRequest.ServiceUpdateItem serviceUpdateItem : serviceRequest.getUpdates()){
            ServiceOffering offering = serviceMap.get(serviceUpdateItem.getServiceId());
            if(offering==null){
                errorMap.put("id_" + serviceUpdateItem.getServiceId(), "Service not found");
                //find owning salon and then its ownerid
            } else if (offering.getIsActive().equals(currentUserId)) {
                errorMap.put("id_" + offering.getId(), "Not authorized to update this service");
            }
            if(!errorMap.isEmpty()){
                throw new BatchValidationException(errorMap);
            }
        }
        List<ServiceOffering> serviceOfferingList =repository.saveAll(serviceMap.values());
        BulkUpdateServiceResponse serviceResponse = new BulkUpdateServiceResponse();
        serviceResponse.setUpdatedCount((long) serviceOfferingList.size());
        List<Long> idList  = serviceOfferingList.stream().map(ServiceOffering::getId).toList();
        eventProducer.publishBulkServiceUpdateEvent(new DeleteServiceEvent());
        serviceResponse.setUpdatedIds(idList);
        return serviceResponse;
    }

    @Override
    public ServiceResponse cloneService(Long id, CloneServiceRequest cloneServiceRequest) {
        ServiceOffering serviceOffering = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","Service Not found with given id"));
        ServiceOffering newClonedService = ServiceOffering.builder()
                .clonedFromId(serviceOffering.getId())
                .serviceType(serviceOffering.getServiceType())
                .salonId(serviceOffering.getSalonId())
                .categoryId(cloneServiceRequest.getCategoryId())
                .currency(serviceOffering.getCurrency())
                .bookingCount(0)
                .depositAmount(serviceOffering.getDepositAmount())
                .name(cloneServiceRequest.getName())
                .description(cloneServiceRequest.getDescription())
                .price(cloneServiceRequest.getPrice())
                .isFeatured(cloneServiceRequest.getIsFeatured())
                .durationMinutes(cloneServiceRequest.getDurationMinutes())
                .build();
        ServiceOffering persistedService = repository.save(newClonedService);
        ServicePriceHistory priceHistory = ServicePriceHistory.builder()
                .changedAt(LocalDateTime.now())
                .oldPrice(null)
                .serviceId(persistedService.getId())
                .build();
        eventProducer.publishBulkServiceUpdateEvent(new DeleteServiceEvent());
        historyRepository.save(priceHistory);
        return serviceMapper.toResponse(persistedService);
    }

    @Override
    public SearchServiceResponseList searchService(ServiceSearchRequest request) {
        return null;
    }

}
