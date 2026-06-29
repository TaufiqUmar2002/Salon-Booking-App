package com.umar.service_offering.service;

import com.umar.events.services.*;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.services.*;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.category.CategoryResponse;
import com.umar.payload.response.services.BulkUpdateServiceResponse;
import com.umar.payload.response.services.SearchServiceResponseList;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.service_offering.event.ServiceOfferingEventProducer;
import com.umar.service_offering.exchange.CategoryClient;
import com.umar.service_offering.exchange.UserClient;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingService implements IServiceOfferingService {

    private final ServiceOfferingRepository repository;
    private final CategoryClient categoryClient;
    private final ServiceMapper serviceMapper;
    private final ServicePriceHistoryRepository historyRepository;
    private final ServiceOfferingEventProducer eventProducer;
    private final UserClient userClient;
    private final Executor executor;

    @Override
    public ServiceResponse createServiceOffering(CreateServiceRequest request) {
        UserValidateResponse response = this.userClient.getUserValidation();
        CategoryResponse categoryResponse =categoryClient.getCategoryById(request.getCategoryId());
        if(!categoryResponse.getIsActive()){
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"CATEGORY_INACTIVE","category.inActive");
        }
        if(request.getRequiresDeposit()==Boolean.TRUE && request.getDepositAmount() ==null){
            throw new ApiException(HttpStatus.BAD_REQUEST,"DEPOSIT_INCONSISTENT","service.depositInconsistent");
        }
        if(request.getAvailableFromTime() !=null && request.getAvailableToTime()!=null && request.getAvailableFromTime().isAfter(request.getAvailableToTime())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"TIME_INCONSISTENT","service.timeInconsistent");

        }
        ServiceOffering serviceOffering =ServiceOffering.builder()
                .serviceType(null)
                .bufferAfterMinutes(0)
                .bufferBeforeMinutes(0)
                .isActive(Boolean.TRUE)
                .requiredDeposit(request.getRequiresDeposit())
                .depositAmount(request.getDepositAmount())
                .name(request.getName())
                .description(request.getDescription())
                .categoryId(request.getCategoryId())
                .build();
        ServiceOffering savedServiceOffering = repository.save(serviceOffering);
        ServicePriceHistory servicePriceHistory = ServicePriceHistory.builder()
                .serviceId(savedServiceOffering.getId())
                .newPrice(savedServiceOffering.getPrice())
                .build();
        historyRepository.save(servicePriceHistory);
        executor.execute(()->{
            ServiceCreatedEvent event = ServiceCreatedEvent.builder()
                    .categoryId(request.getCategoryId())
                    .serviceId(savedServiceOffering.getId())
                    .isActive(Boolean.TRUE)
                    .name(request.getName())
                    .price(request.getPrice())
                    .build();
            eventProducer.publishServiceCreatedEvent(event);
        });
        return serviceMapper.toResponse(savedServiceOffering);
    }

    @Override
    public Page<ServiceResponse> getAllServicesBySalonId(Long salonId, ServiceSalonSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(),request.getSize(), Sort.Direction.fromString(request.getSortBy()));
        Page<ServiceOffering> serviceOfferingPage = repository.searchServiceBySalonId(salonId,request.getCategoryId(),pageable);
        return serviceOfferingPage.map(serviceMapper::toResponse);
    }

    @Override
    public ServiceResponse getAllServicesById(Long id) {
        ServiceOffering serviceOffering = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","service.notFound"));
        return serviceMapper.toResponse(serviceOffering);
    }

    @Override
    public ServiceResponse updateService(Long id, UpdateServiceRequest request) {
        ServiceOffering serviceOffering = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","service.notFound"));
        if(request.getRequiresDeposit() && (request.getDepositAmount()==null || request.getDepositAmount().equals(BigDecimal.ZERO))){
            throw new ApiException(HttpStatus.BAD_REQUEST,"DEPOSIT_INCONSISTENT","service.depositInconsistent");
        }
        CategoryResponse categoryResponse = categoryClient.getCategoryById(request.getCategoryId());
        if(!categoryResponse.getIsActive()){
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"CATEGORY_INACTIVE","category.inActive");
        }
        ServiceOffering existingService = this.repository.findServiceOfferingByName(request.getName());
        if(existingService!=null){
            throw new ApiException(HttpStatus.BAD_REQUEST,"DUPLICATE_NAME","service.duplicateName");
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
        executor.execute(()->{
            UpdateServiceEvent updateServiceEvent = UpdateServiceEvent.builder()
                            .serviceId(serviceOffering.getId())
                            .salonId(serviceOffering.getSalonId())
                            .build();
            eventProducer.publishServiceUpdateEvent(updateServiceEvent);
        });
        return serviceMapper.toResponse(serviceOffering);
    }

    @Override
    public void deleteService(Long id, DeleteServiceRequest request) {
        ServiceOffering serviceOffering = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SERVICE_NOT_FOUND","service.notFound"));
        if(!serviceOffering.getIsActive()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"ALREADY_INACTIVE","service.alreadyInactive");
        }
        serviceOffering.setIsActive(false);
        repository.save(serviceOffering);
        executor.execute(()->{
            DeleteServiceEvent deleteServiceEvent = DeleteServiceEvent.builder()
                            .serviceId(serviceOffering.getId())
                            .salonId(serviceOffering.getSalonId())
                            .build();
            eventProducer.publishServiceDeleteEvent(deleteServiceEvent);
        });
    }

    @Override
    public BulkUpdateServiceResponse bulkUpdateService(BulkUpdateServiceRequest serviceRequest) {
        Map<String, String> errorMap = new HashMap<>();
        UserValidateResponse response = userClient.getUserValidation();;
        if(serviceRequest.getUpdates().size()>50){
            throw new ApiException(HttpStatus.BAD_REQUEST,"BATCH_TOO_LARGE","service.batchToLarge");
        }
        List<Long> ids  = serviceRequest.getUpdates().stream().map(BulkUpdateServiceRequest.ServiceUpdateItem::getServiceId).toList();
        List<ServiceOffering> existingServices = repository.findAllById(ids);
        Long currentUserId = response.getUserId();
        Map<Long,ServiceOffering> serviceMap = existingServices.stream().collect(Collectors.toMap(ServiceOffering::getId,s->s));
        for(BulkUpdateServiceRequest.ServiceUpdateItem serviceUpdateItem : serviceRequest.getUpdates()){
            ServiceOffering offering = serviceMap.get(serviceUpdateItem.getServiceId());
            if(offering==null){
                errorMap.put("id_" + serviceUpdateItem.getServiceId(), "Service not found");
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
        executor.execute(()->{
            BulkServiceUpdateEvent updateEvent =BulkServiceUpdateEvent.builder()
                    .affectedServiceIds(idList)
                    .updatedAt(LocalDateTime.now())
                    .build();
            eventProducer.publishBulkServiceUpdateEvent(updateEvent);
        });
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
        executor.execute(()->{
            CloneServiceEvent cloneServiceEvent =CloneServiceEvent.builder()
                    .sourceServiceId(id)
                    .newServiceId(persistedService.getId())
                    .salonId(serviceOffering.getSalonId())
                    .build();
            eventProducer.publishCloneServiceEvent(cloneServiceEvent);
        });
        historyRepository.save(priceHistory);
        return serviceMapper.toResponse(persistedService);
    }

    @Override
    public SearchServiceResponseList searchService(ServiceSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        List<ServiceOffering> serviceOfferingList = this.repository.searchServices(request.getQuery(), request.getSalonId(),
                request.getCategoryId(), request.getMinPrice(), request.getMaxPrice(), request.getMaxDuration(), pageable);
        SearchServiceResponseList responseList = new SearchServiceResponseList();
        responseList.setResponseList(serviceOfferingList.stream().map(serviceMapper::toSearchResponse).toList());
        return responseList;
    }

    public String getCurrentLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal()!=null && authentication.getPrincipal() instanceof String userDetails){
            return userDetails;
        }
        return null;
    }

}
