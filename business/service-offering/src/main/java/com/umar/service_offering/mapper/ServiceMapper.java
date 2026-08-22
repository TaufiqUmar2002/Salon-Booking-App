package com.umar.service_offering.mapper;

import com.umar.payload.request.services.CreateServiceRequest;
import com.umar.payload.request.services.UpdateServiceRequest;
import com.umar.payload.response.services.SearchServiceResponseList;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.service_offering.model.ServiceOffering;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(source = "price",target = "currentPrice")
    @Mapping(source = "id",target = "serviceId")
    ServiceResponse toResponse(ServiceOffering serviceOffering);

    ServiceOffering toEntity(CreateServiceRequest serviceRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void UpdateServiceFromRequest(UpdateServiceRequest request, @MappingTarget ServiceOffering serviceOffering);

    @Mapping(source = "id",target = "serviceId")
    @Mapping(source = "name",target = "serviceName")
    SearchServiceResponseList.SearchServiceResponse toSearchResponse(ServiceOffering serviceOffering);
}
