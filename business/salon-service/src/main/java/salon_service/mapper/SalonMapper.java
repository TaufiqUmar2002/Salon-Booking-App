package salon_service.mapper;


import com.umar.payload.request.salon.SalonRequest;
import com.umar.payload.request.salon.UpdateSalonRequest;
import com.umar.payload.response.salon.SalonResponse;
import com.umar.payload.response.salon.SalonResponseV1;
import org.mapstruct.*;
import salon_service.model.Salon;

@Mapper(componentModel = "spring")
public interface SalonMapper {

    @Mapping(target = "categoryId",source = "category")
    Salon toEntity(SalonRequest request);

    @Mapping(target = "salonId",source = "id")
    SalonResponse toResponse(Salon salon);
    @Mapping(target = "salonId",source = "id")
    @Mapping(target = "address",source = "salon")
    SalonResponseV1 toResponseV1(Salon salon);

    default String mapAddress(Salon salon) {
        if (salon == null) {
            return null;
        }
        return salon.getAddressLine1() + " " + salon.getAddressLine2();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSalonFromDto(UpdateSalonRequest request, @MappingTarget Salon salon);

}
