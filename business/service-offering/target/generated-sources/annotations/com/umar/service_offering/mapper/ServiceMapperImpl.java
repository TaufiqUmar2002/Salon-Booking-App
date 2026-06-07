package com.umar.service_offering.mapper;

import com.umar.payload.request.services.UpdateServiceRequest;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.service_offering.model.ServiceOffering;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-02T08:40:16+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public ServiceResponse toResponse(ServiceOffering serviceOffering) {
        if ( serviceOffering == null ) {
            return null;
        }

        ServiceResponse.ServiceResponseBuilder serviceResponse = ServiceResponse.builder();

        serviceResponse.serviceId( serviceOffering.getId() );
        serviceResponse.name( serviceOffering.getName() );
        serviceResponse.categoryId( serviceOffering.getCategoryId() );
        serviceResponse.durationMinutes( serviceOffering.getDurationMinutes() );
        serviceResponse.isActive( serviceOffering.getIsActive() );
        serviceResponse.isFeatured( serviceOffering.getIsFeatured() );
        serviceResponse.imageUrl( serviceOffering.getImageUrl() );

        return serviceResponse.build();
    }

    @Override
    public void UpdateServiceFromRequest(UpdateServiceRequest request, ServiceOffering serviceOffering) {
        if ( request == null ) {
            return;
        }

        if ( request.getCategoryId() != null ) {
            serviceOffering.setCategoryId( request.getCategoryId() );
        }
        if ( request.getName() != null ) {
            serviceOffering.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            serviceOffering.setDescription( request.getDescription() );
        }
        if ( request.getDurationMinutes() != null ) {
            serviceOffering.setDurationMinutes( request.getDurationMinutes() );
        }
        if ( request.getPrice() != null ) {
            serviceOffering.setPrice( request.getPrice() );
        }
        if ( request.getDiscountedPrice() != null ) {
            serviceOffering.setDiscountedPrice( request.getDiscountedPrice() );
        }
        if ( request.getMaxCapacity() != null ) {
            serviceOffering.setMaxCapacity( request.getMaxCapacity() );
        }
        if ( serviceOffering.getStaffIds() != null ) {
            List<Long> list = request.getStaffIds();
            if ( list != null ) {
                serviceOffering.getStaffIds().clear();
                serviceOffering.getStaffIds().addAll( list );
            }
        }
        else {
            List<Long> list = request.getStaffIds();
            if ( list != null ) {
                serviceOffering.setStaffIds( new ArrayList<Long>( list ) );
            }
        }
        if ( request.getDepositAmount() != null ) {
            serviceOffering.setDepositAmount( request.getDepositAmount() );
        }
        if ( request.getIsFeatured() != null ) {
            serviceOffering.setIsFeatured( request.getIsFeatured() );
        }
        if ( serviceOffering.getTags() != null ) {
            List<String> list1 = request.getTags();
            if ( list1 != null ) {
                serviceOffering.getTags().clear();
                serviceOffering.getTags().addAll( list1 );
            }
        }
        else {
            List<String> list1 = request.getTags();
            if ( list1 != null ) {
                serviceOffering.setTags( new ArrayList<String>( list1 ) );
            }
        }
    }
}
