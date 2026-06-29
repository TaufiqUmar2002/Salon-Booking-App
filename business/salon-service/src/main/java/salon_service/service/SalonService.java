package salon_service.service;


import com.umar.events.salon.SalonCreatedEvent;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.salon.DeleteSalonRequest;
import com.umar.payload.request.salon.SalonRequest;
import com.umar.payload.request.salon.SalonSearchRequest;
import com.umar.payload.request.salon.UpdateSalonRequest;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.salon.SalonResponse;
import com.umar.payload.response.salon.SalonResponseData;
import com.umar.payload.response.salon.SalonResponseList;
import com.umar.payload.response.salon.SalonResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import salon_service.event.SalonEventProducer;
import salon_service.exchange.UserClient;
import salon_service.mapper.SalonMapper;
import salon_service.model.Salon;
import salon_service.model.SalonAuditLog;
import salon_service.repository.SalonAuditRepository;
import salon_service.repository.SalonRepository;
import salon_service.serviceinterface.ISalonService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;


@Service
@RequiredArgsConstructor
public class SalonService implements ISalonService {

    private final SalonRepository salonRepository;
    private final UserClient userClient;
    private final SalonMapper mapper;
    private final SalonEventProducer eventProducer;
    private final SalonAuditRepository auditRepository;
    private final Executor executor;


    @Override
    public SalonResponse createSalon(SalonRequest request) {
        UserValidateResponse response = userClient.getUserValidation();
        Optional<Salon> ifPresent = salonRepository.findSalonByNameAndOwnerId(request.getName(),response.getUserId());
        if(ifPresent.isPresent()){
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"DUPLICATE_REQUEST","salon.alreadyExists");
        }
        Salon salon = mapper.toEntity(request);
        salon.setIsActive(false);
        Salon savedSalon = salonRepository.save(salon);
        SalonResponse salonResponse = mapper.toResponse(savedSalon);
        salonResponse.setMessage("salon.registrationSubmitted");
        executor.execute(()->{
            SalonCreatedEvent salonCreatedEvent = new SalonCreatedEvent();
            eventProducer.publishSalonCreationEvent(salonCreatedEvent);
        });
        return salonResponse;
    }

    @Override
    public SalonResponseList searchSalon(SalonSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Double lat = request.getLatitude();
        Double lng = request.getLongitude();
        Double radius = request.getRadiusInKm();
        if (lat == null || lng == null || radius == null) {
            lat = null;
            lng = null;
            radius = null;
        }
        Page<Salon> salonPage = salonRepository.findBrowseSalons(request.getCategory(), lat, lng, radius, pageable);
        return convertToSalonResponseList(salonPage);
    }

    @Override
    public SalonResponseV1 getSalonById(Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SALON_NOT_FOUND","No Salon found with given id"));
        return mapper.toResponseV1(salon);
    }

    @Override
    public SalonResponseV1 updateSalon(UpdateSalonRequest request, Long salonId) {
        Salon salon = salonRepository.findById(salonId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SALON_NOT_FOUND","No Salon found with given id"));
        if(!salon.getOwnerId().equals(21323l)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"FORBIDDEN","SALON_OWNER trying update another owner details");
        }
        mapper.updateSalonFromDto(request,salon);
        salonRepository.save(salon);
        SalonCreatedEvent salonCreatedEvent = new SalonCreatedEvent();
        eventProducer.publishSalonCreationEvent(salonCreatedEvent);
        return mapper.toResponseV1(salon);
    }

    @Override
    public void deleteSalon(DeleteSalonRequest request, Long salonId) {
        Salon salon = salonRepository.findById(salonId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SALON_NOT_FOUND", "salon.notFound"));
        if (!salon.getIsActive()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "ALREADY_INACTIVE", "salon.inactive");
        }
        salon.setIsActive(false);
        salonRepository.save(salon);
        SalonAuditLog auditLog = SalonAuditLog.builder()
                .action("DEACTIVATE")
                .ownerId(salon.getOwnerId())
                .reason(request.getReason())
                .timeStamp(LocalDateTime.now())
                .build();
        auditRepository.save(auditLog);
        SalonCreatedEvent salonCreatedEvent = new SalonCreatedEvent();
        eventProducer.publishSalonCreationEvent(salonCreatedEvent);
    }

    @Override
    public SalonResponseList getSalonByCategory(Long salonId) {
        return null;
    }

    public SalonResponseList convertToSalonResponseList(Page<Salon> salonPage) {
        List<SalonResponseData> salonResponseDataList =
                salonPage.getContent()
                        .stream()
                        .map(this::convertToSalonResponseData)
                        .toList();
        SalonResponseList response = new SalonResponseList();
        response.setSalonResponseDataList(salonResponseDataList);
        response.setTotalElements(salonPage.getTotalElements());
        response.setTotalPages(salonPage.getTotalPages());
        response.setCurrentPage(salonPage.getNumber());
        return response;
    }

    private SalonResponseData convertToSalonResponseData(Salon salon) {
        return SalonResponseData.builder()
                .salonId(salon.getId())
                .name(salon.getName())
                .city(salon.getCity())
                .category(salon.getCategoryId().toString())
                .totalReviews(salon.getTotalReviews())
                .averageRating(salon.getAverageRating())
                .build();
    }

}
