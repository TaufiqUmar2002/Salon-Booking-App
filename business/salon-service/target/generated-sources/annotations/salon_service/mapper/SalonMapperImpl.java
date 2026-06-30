package salon_service.mapper;

import com.umar.payload.request.salon.SalonRequest;
import com.umar.payload.request.salon.UpdateSalonRequest;
import com.umar.payload.response.salon.SalonResponse;
import com.umar.payload.response.salon.SalonResponseV1;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import salon_service.model.Salon;
import salon_service.model.ServiceSummary;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-30T23:46:28+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class SalonMapperImpl implements SalonMapper {

    @Override
    public Salon toEntity(SalonRequest request) {
        if ( request == null ) {
            return null;
        }

        Salon.SalonBuilder salon = Salon.builder();

        salon.name( request.getName() );
        salon.description( request.getDescription() );
        salon.categoryId( request.getCategoryId() );
        salon.addressLine1( request.getAddressLine1() );
        salon.city( request.getCity() );
        salon.state( request.getState() );
        salon.postalCode( request.getPostalCode() );
        salon.latitude( request.getLatitude() );
        salon.longitude( request.getLongitude() );
        salon.phone( request.getPhone() );
        salon.email( request.getEmail() );
        salon.website( request.getWebsite() );
        Map<String, String> map = request.getOpeningHours();
        if ( map != null ) {
            salon.openingHours( new LinkedHashMap<String, String>( map ) );
        }
        salon.services( serviceSummaryListToServiceSummaryList( request.getServices() ) );
        List<String> list1 = request.getGalleryUrls();
        if ( list1 != null ) {
            salon.galleryUrls( new ArrayList<String>( list1 ) );
        }

        return salon.build();
    }

    @Override
    public SalonResponse toResponse(Salon salon) {
        if ( salon == null ) {
            return null;
        }

        SalonResponse.SalonResponseBuilder salonResponse = SalonResponse.builder();

        salonResponse.salonId( salon.getId() );
        salonResponse.name( salon.getName() );
        salonResponse.isVerified( salon.getIsVerified() );

        return salonResponse.build();
    }

    @Override
    public SalonResponseV1 toResponseV1(Salon salon) {
        if ( salon == null ) {
            return null;
        }

        SalonResponseV1.SalonResponseV1Builder salonResponseV1 = SalonResponseV1.builder();

        salonResponseV1.salonId( salon.getId() );
        salonResponseV1.address( mapAddress( salon ) );
        salonResponseV1.name( salon.getName() );
        salonResponseV1.description( salon.getDescription() );
        salonResponseV1.phone( salon.getPhone() );
        salonResponseV1.email( salon.getEmail() );
        salonResponseV1.website( salon.getWebsite() );
        Map<String, String> map = salon.getOpeningHours();
        if ( map != null ) {
            salonResponseV1.openingHours( new LinkedHashMap<String, String>( map ) );
        }
        salonResponseV1.services( serviceSummaryListToServiceSummaryList1( salon.getServices() ) );
        List<String> list1 = salon.getGalleryUrls();
        if ( list1 != null ) {
            salonResponseV1.galleryUrls( new ArrayList<String>( list1 ) );
        }
        salonResponseV1.averageRating( salon.getAverageRating() );
        salonResponseV1.totalReviews( salon.getTotalReviews() );
        salonResponseV1.isVerified( salon.getIsVerified() );

        return salonResponseV1.build();
    }

    @Override
    public void updateSalonFromDto(UpdateSalonRequest request, Salon salon) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            salon.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            salon.setDescription( request.getDescription() );
        }
        if ( request.getCategoryId() != null ) {
            salon.setCategoryId( request.getCategoryId() );
        }
        if ( request.getAddressLine1() != null ) {
            salon.setAddressLine1( request.getAddressLine1() );
        }
        if ( request.getCity() != null ) {
            salon.setCity( request.getCity() );
        }
        if ( request.getState() != null ) {
            salon.setState( request.getState() );
        }
        if ( request.getPostalCode() != null ) {
            salon.setPostalCode( request.getPostalCode() );
        }
        if ( request.getLatitude() != null ) {
            salon.setLatitude( request.getLatitude() );
        }
        if ( request.getLongitude() != null ) {
            salon.setLongitude( request.getLongitude() );
        }
        if ( request.getPhone() != null ) {
            salon.setPhone( request.getPhone() );
        }
        if ( request.getEmail() != null ) {
            salon.setEmail( request.getEmail() );
        }
        if ( request.getWebsite() != null ) {
            salon.setWebsite( request.getWebsite() );
        }
        if ( salon.getOpeningHours() != null ) {
            Map<String, String> map = request.getOpeningHours();
            if ( map != null ) {
                salon.getOpeningHours().clear();
                salon.getOpeningHours().putAll( map );
            }
        }
        else {
            Map<String, String> map = request.getOpeningHours();
            if ( map != null ) {
                salon.setOpeningHours( new LinkedHashMap<String, String>( map ) );
            }
        }
        if ( salon.getServices() != null ) {
            List<ServiceSummary> list = serviceSummaryListToServiceSummaryList( request.getServices() );
            if ( list != null ) {
                salon.getServices().clear();
                salon.getServices().addAll( list );
            }
        }
        else {
            List<ServiceSummary> list = serviceSummaryListToServiceSummaryList( request.getServices() );
            if ( list != null ) {
                salon.setServices( list );
            }
        }
        if ( request.getIsVerified() != null ) {
            salon.setIsVerified( request.getIsVerified() );
        }
    }

    protected ServiceSummary serviceSummaryToServiceSummary(com.umar.payload.request.salon.ServiceSummary serviceSummary) {
        if ( serviceSummary == null ) {
            return null;
        }

        ServiceSummary.ServiceSummaryBuilder serviceSummary1 = ServiceSummary.builder();

        serviceSummary1.durationMinutes( serviceSummary.getDurationMinutes() );
        serviceSummary1.price( serviceSummary.getPrice() );

        return serviceSummary1.build();
    }

    protected List<ServiceSummary> serviceSummaryListToServiceSummaryList(List<com.umar.payload.request.salon.ServiceSummary> list) {
        if ( list == null ) {
            return null;
        }

        List<ServiceSummary> list1 = new ArrayList<ServiceSummary>( list.size() );
        for ( com.umar.payload.request.salon.ServiceSummary serviceSummary : list ) {
            list1.add( serviceSummaryToServiceSummary( serviceSummary ) );
        }

        return list1;
    }

    protected com.umar.payload.request.salon.ServiceSummary serviceSummaryToServiceSummary1(ServiceSummary serviceSummary) {
        if ( serviceSummary == null ) {
            return null;
        }

        com.umar.payload.request.salon.ServiceSummary.ServiceSummaryBuilder serviceSummary1 = com.umar.payload.request.salon.ServiceSummary.builder();

        serviceSummary1.durationMinutes( serviceSummary.getDurationMinutes() );
        serviceSummary1.price( serviceSummary.getPrice() );

        return serviceSummary1.build();
    }

    protected List<com.umar.payload.request.salon.ServiceSummary> serviceSummaryListToServiceSummaryList1(List<ServiceSummary> list) {
        if ( list == null ) {
            return null;
        }

        List<com.umar.payload.request.salon.ServiceSummary> list1 = new ArrayList<com.umar.payload.request.salon.ServiceSummary>( list.size() );
        for ( ServiceSummary serviceSummary : list ) {
            list1.add( serviceSummaryToServiceSummary1( serviceSummary ) );
        }

        return list1;
    }
}
