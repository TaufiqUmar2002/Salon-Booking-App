package com.umar.service_offering.mapper;

import com.umar.payload.request.services.CreateServiceRequest;
import com.umar.payload.request.services.UpdateServiceRequest;
import com.umar.payload.response.services.SearchServiceResponseList;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.service_offering.model.ServiceOffering;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-22T21:56:34+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public ServiceResponse toResponse(ServiceOffering serviceOffering) {
        if ( serviceOffering == null ) {
            return null;
        }

        ServiceResponse.ServiceResponseBuilder serviceResponse = ServiceResponse.builder();

        serviceResponse.currentPrice( serviceOffering.getPrice() );
        serviceResponse.serviceId( serviceOffering.getId() );
        serviceResponse.name( serviceOffering.getName() );
        serviceResponse.categoryId( serviceOffering.getCategoryId() );
        serviceResponse.durationMinutes( serviceOffering.getDurationMinutes() );
        serviceResponse.isActive( serviceOffering.getIsActive() );
        serviceResponse.isFeatured( serviceOffering.getIsFeatured() );
        serviceResponse.bookingCount( serviceOffering.getBookingCount() );
        serviceResponse.imageUrl( serviceOffering.getImageUrl() );

        return serviceResponse.build();
    }

    @Override
    public ServiceOffering toEntity(CreateServiceRequest serviceRequest) {
        if ( serviceRequest == null ) {
            return null;
        }

        ServiceOffering.ServiceOfferingBuilder serviceOffering = ServiceOffering.builder();

        serviceOffering.salonId( serviceRequest.getSalonId() );
        serviceOffering.categoryId( serviceRequest.getCategoryId() );
        serviceOffering.name( serviceRequest.getName() );
        serviceOffering.description( serviceRequest.getDescription() );
        serviceOffering.durationMinutes( serviceRequest.getDurationMinutes() );
        serviceOffering.price( serviceRequest.getPrice() );
        serviceOffering.discountedPrice( serviceRequest.getDiscountedPrice() );
        serviceOffering.currency( serviceRequest.getCurrency() );
        serviceOffering.maxCapacity( serviceRequest.getMaxCapacity() );
        List<Long> list = serviceRequest.getStaffIds();
        if ( list != null ) {
            serviceOffering.staffIds( new ArrayList<Long>( list ) );
        }
        serviceOffering.depositAmount( serviceRequest.getDepositAmount() );
        serviceOffering.isFeatured( serviceRequest.getIsFeatured() );
        List<String> list1 = serviceRequest.getTags();
        if ( list1 != null ) {
            serviceOffering.tags( new ArrayList<String>( list1 ) );
        }
        serviceOffering.availableFromTime( serviceRequest.getAvailableFromTime() );
        serviceOffering.availableToTime( serviceRequest.getAvailableToTime() );

        return serviceOffering.build();
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
        if ( request.getAvailableFromTime() != null ) {
            serviceOffering.setAvailableFromTime( request.getAvailableFromTime() );
        }
        if ( request.getAvailableToTime() != null ) {
            serviceOffering.setAvailableToTime( request.getAvailableToTime() );
        }
    }

    @Override
    public SearchServiceResponseList.SearchServiceResponse toSearchResponse(ServiceOffering serviceOffering) {
        if ( serviceOffering == null ) {
            return null;
        }

        SearchServiceResponseList.SearchServiceResponse.SearchServiceResponseBuilder searchServiceResponse = SearchServiceResponseList.SearchServiceResponse.builder();

        searchServiceResponse.serviceId( serviceOffering.getId() );
        searchServiceResponse.serviceName( serviceOffering.getName() );

        return searchServiceResponse.build();
    }
}
