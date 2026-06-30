package salon_service.service;

import com.umar.events.salon.SalonCreatedEvent;
import com.umar.events.salon.SalonDeletedEvent;
import com.umar.events.salon.SalonUpdatedEvent;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.enums.user.UserRole;
import com.umar.payload.request.salon.DeleteSalonRequest;
import com.umar.payload.request.salon.SalonRequest;
import com.umar.payload.request.salon.SalonSearchRequest;
import com.umar.payload.request.salon.UpdateSalonRequest;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.category.CategoryResponse;
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
import salon_service.exchange.CategoryClient;
import salon_service.exchange.UserClient;
import salon_service.mapper.SalonMapper;
import salon_service.model.Salon;
import salon_service.model.SalonAuditLog;
import salon_service.model.ServiceSummary;
import salon_service.repository.SalonAuditRepository;
import salon_service.repository.SalonRepository;
import salon_service.serviceinterface.ISalonService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class SalonService implements ISalonService {

    private static final Pattern HOURS_PATTERN = Pattern.compile("^(CLOSED|([01]?\\d|2[0-3]):[0-5]\\d-([01]?\\d|2[0-3]):[0-5]\\d)$");
    private final SalonRepository salonRepository;
    private final UserClient userClient;
    private final SalonMapper mapper;
    private final SalonEventProducer eventProducer;
    private final SalonAuditRepository auditRepository;
    private final Executor executor;
    private final CategoryClient categoryClient;


    @Override
    public SalonResponse createSalon(SalonRequest request) {
        UserValidateResponse response = userClient.getUserValidation();
        Optional<Salon> ifPresent = salonRepository.findSalonByNameAndOwnerId(request.getName(),response.getUserId());
        if(ifPresent.isPresent()){
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"DUPLICATE_REQUEST","salon.alreadyExists");
        }
        CategoryResponse categoryResponse = categoryClient.getCategoryById(request.getCategoryId());
        if(categoryResponse==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"NOT_FOUND","salon.categoryNotFound");
        }
        if((request.getLatitude()!=null && request.getLongitude()==null) || (request.getLongitude()!=null && request.getLatitude()==null)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"GPS_INCOMPLETE","salon.location");
        }
        String formedSlug = request.getName().toLowerCase().trim() + response.getUserId();
        if(salonRepository.existsBySlug(formedSlug)>0){
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"DUPLICATE_REQUEST","salon.duplicateSlug");
        }
        this.validateOpeningHours(request.getOpeningHours());
        Salon salon = mapper.toEntity(request);
        salon.setSlug(formedSlug);
        salon.setTotalReviews(0);
        salon.setIsActive(false);
        salon.setOwnerId(response.getUserId());
        salon.setIsVerified(false);
        List<ServiceSummary> serviceSummaryList = new ArrayList<>();
        ServiceSummary serviceSummary = new ServiceSummary();
        request.getServices().forEach(service->{
            serviceSummary.setName(service.getServiceName());
            serviceSummary.setPrice(service.getPrice());
            serviceSummary.setDurationMinutes(service.getDurationMinutes());
            serviceSummaryList.add(serviceSummary);
        });
        salon.setServices(serviceSummaryList);
        Salon savedSalon = salonRepository.save(salon);
        SalonResponse salonResponse = mapper.toResponse(savedSalon);
        SalonAuditLog salonAuditLog =SalonAuditLog.builder()
                        .salonId(savedSalon.getId())
                        .action("CREATE")
                        .reason(request.getReason())
                        .ownerId(response.getUserId())
                        .build();
        auditRepository.save(salonAuditLog);
        salonResponse.setMessage("salon.registrationSubmitted");
        executor.execute(()->{
            SalonCreatedEvent salonCreatedEvent = SalonCreatedEvent.builder()
                    .salonId(savedSalon.getId())
                    .ownerId(response.getUserId())
                    .categoryId(request.getCategoryId())
                    .name(request.getName())
                    .slug(formedSlug)
                    .isVerified(false)
                    .reason(request.getReason())
                    .build();
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
        if((lat!=null && lng==null) || (lng!=null && lat==null)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"GPS_INCOMPLETE","salon.location");
        }
        Page<Salon> salonPage = salonRepository.findBrowseSalons(request.getCategory(), lat, lng, radius, pageable);
        return convertToSalonResponseList(salonPage);
    }

    @Override
    public SalonResponseV1 getSalonById(Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SALON_NOT_FOUND","salon.notFound"));
        return mapper.toResponseV1(salon);
    }

    @Override
    public SalonResponseV1 updateSalon(UpdateSalonRequest request, Long salonId) {
        Salon salon = salonRepository.findById(salonId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"SALON_NOT_FOUND","salon.notFound"));
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        if(userValidateResponse.getRole().equals(UserRole.SALON_OWNER.name()) &&!salon.getOwnerId().equals(userValidateResponse.getUserId())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"FORBIDDEN","salon.ownerMismatch");
        }
        if(request.getIsVerified()!=null && !userValidateResponse.getRole().equals(UserRole.ADMIN.name())){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","salon.adminOnly");
        }
        if((request.getLatitude()!=null && request.getLongitude()==null) || (request.getLongitude()!=null && request.getLatitude()==null)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"GPS_INCOMPLETE","salon.location");
        }
        this.validateOpeningHours(request.getOpeningHours());
        List<ServiceSummary> serviceSummaryList = new ArrayList<>();
        ServiceSummary serviceSummary = new ServiceSummary();
        String slug =salon.getSlug();
        if(request.getName()!=null){
            slug = request.getName().toLowerCase().trim() + userValidateResponse.getUserId();
            if(salonRepository.existsBySlug(slug)>0){
                throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"DUPLICATE_REQUEST","salon.duplicateSlug");
            }
        }
        mapper.updateSalonFromDto(request,salon);
        salon.setSlug(slug);
        salonRepository.save(salon);
        SalonAuditLog salonAuditLog = SalonAuditLog.builder()
                        .action("UPDATE")
                        .ownerId(salon.getOwnerId())
                        .salonId(salonId)
                        .reason(request.getReason())
                        .build();
        auditRepository.save(salonAuditLog);
        executor.execute(()->{
            SalonUpdatedEvent salonUpdatedEvent = SalonUpdatedEvent.builder()
                    .ownerId(salon.getOwnerId())
                    .salonId(salonId)
                    .updatedAt(LocalDateTime.now())
                    .UpdatedBy(userValidateResponse.getUserId())
                    .build();
            eventProducer.publishSalonUpdatedEvent(salonUpdatedEvent);
        });
        return mapper.toResponseV1(salon);
    }

    @Override
    public void deleteSalon(DeleteSalonRequest request, Long salonId) {
        UserValidateResponse response = userClient.getUserValidation();
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
        executor.execute(()->{
            SalonDeletedEvent salonDeletedEvent = SalonDeletedEvent
                    .builder()
                            .salonId(salonId)
                                    .deactivatedBy(response.getUserId())
                                            .deactivatedAt(LocalDateTime.now())
                                                    .build();
            eventProducer.publishSalonDeletedEvent(salonDeletedEvent);
        });

    }

    @Override
    public SalonResponseList getSalonByCategory(Long categoryId) {
        List<Salon> salonList = this.salonRepository.findByCategoryId(categoryId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SALON_NOT_FOUND", "salon.notFound"));
        SalonResponseList salonResponseList = new SalonResponseList();
        salonResponseList.setSalonResponseDataList(new ArrayList<>());
        salonList.forEach(salon -> {
            SalonResponseData salonResponseData = convertToSalonResponseData(salon);
            salonResponseList.getSalonResponseDataList().add(salonResponseData);
        });
        return salonResponseList;
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

    private void  validateOpeningHours(Map<String,String> openingHours){
        if(openingHours==null || openingHours.isEmpty()){
            return;
        }
        openingHours.forEach((key,value)->{
            if(value==null || value.isEmpty()){
                throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_REQUEST","salon.openingHours");
            }
            if(!HOURS_PATTERN.matcher(value).matches()){
                throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_REQUEST","salon.openingHours.format");
            }
        });
    }

}
