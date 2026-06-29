package com.umar.service_offering.mapper;

import com.umar.payload.request.services.UpdateServiceRequest;
import com.umar.payload.response.services.SearchServiceResponseList;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.payload.response.services.ServiceResponseList;
import com.umar.service_offering.model.ServiceOffering;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(source = "id",target = "serviceId")
    ServiceResponse toResponse(ServiceOffering serviceOffering);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void UpdateServiceFromRequest(UpdateServiceRequest request, @MappingTarget ServiceOffering serviceOffering);

    SearchServiceResponseList.SearchServiceResponse toSearchResponse(ServiceOffering serviceOffering);
}
