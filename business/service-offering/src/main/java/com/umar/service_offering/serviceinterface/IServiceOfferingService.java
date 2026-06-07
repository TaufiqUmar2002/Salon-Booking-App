package com.umar.service_offering.serviceinterface;

import com.umar.payload.request.services.*;
import com.umar.payload.response.services.BulkUpdateServiceResponse;
import com.umar.payload.response.services.SearchServiceResponseList;
import com.umar.payload.response.services.ServiceResponse;
import org.springframework.data.domain.Page;

public interface IServiceOfferingService {

    ServiceResponse createServiceOffering(CreateServiceRequest request);
    Page<ServiceResponse> getAllServicesBySalonId(Long salonId,ServiceSalonSearchRequest request);
    ServiceResponse getAllServicesById(Long id);
    ServiceResponse updateService(Long id, UpdateServiceRequest request);
    void deleteService(Long id, DeleteServiceRequest request);
    BulkUpdateServiceResponse bulkUpdateService(BulkUpdateServiceRequest serviceRequest);
    ServiceResponse cloneService(Long id,CloneServiceRequest cloneServiceRequest);
    SearchServiceResponseList searchService(ServiceSearchRequest request);
}
