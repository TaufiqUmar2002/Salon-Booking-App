package salon_service.service;

import com.umar.exceptions.common.exception.ApiException;
import org.springframework.data.jpa.domain.Specification;
import com.umar.payload.request.salon.ai.SalonAiSearchRequest;
import com.umar.payload.request.salon.ai.SalonGenerateDescriptionRequest;
import com.umar.payload.request.salon.ai.SalonSearchFilters;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.category.CategoryResponse;
import com.umar.payload.response.salon.ai.SalonAiInsightResponse;
import com.umar.payload.response.salon.ai.SalonAiSearchResponse;
import com.umar.payload.response.salon.ai.SalonGenerateDescriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import salon_service.exchange.CategoryClient;
import salon_service.exchange.UserClient;
import salon_service.model.Salon;
import salon_service.repository.SalonRepository;
import salon_service.serviceinterface.ISalonAiService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonAiService implements ISalonAiService {

    private final SalonRepository salonRepository;
    private final ChatClient chatClient;
    private final UserClient userClient;
    private final CategoryClient categoryClient;

    @Value("classpath:prompts/salon-bio.st")
    private  Resource salonBioPrompt;

    @Value("classpath:prompts/salon-search.st")
    private  Resource salonSearchPrompt;

    @Override
    public SalonGenerateDescriptionResponse generateDescription(SalonGenerateDescriptionRequest request) {
        Salon salon =  salonRepository.findById(request.getSalonId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SALON_NOT_FOUND","salon.notFound"));
        UserValidateResponse response = userClient.getUserValidation();
        if(!salon.getOwnerId().equals(response.getUserId())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"FORBIDDEN","salon.ownerMismatch");
        }
        if(request.getKeywords()==null || request.getKeywords().isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"NO_KEYWORDS","salon.ai.keyword");
        }
        CategoryResponse categoryResponse = categoryClient.getCategoryById(salon.getCategoryId());
        return chatClient.prompt()
                .user(userPrompt->userPrompt.text(salonBioPrompt).
                        param("keywords",request.getKeywords())
                .param("name",salon.getName())
                .param("city",salon.getCity())
                .param("category",categoryResponse.getName())
                .param("services",salon.getServices())
                .param("tone",request.getTone())
                .param("maxLength",request.getMaxLength())).call().entity(SalonGenerateDescriptionResponse.class);
    }

    @Override
    public SalonAiInsightResponse getInsights(Long salonId) {
        Salon salon =  salonRepository.findById(salonId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SALON_NOT_FOUND","salon.notFound"));
        return null;
    }

    @Override
    public SalonAiSearchResponse searchSalon(SalonAiSearchRequest request) {
        if((request.getLat()!=null && request.getLng()==null) || (request.getLng()!=null && request.getLat()==null)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"GPS_INCOMPLETE","salon.location");
        }
        SalonSearchFilters filters = chatClient.prompt()
                .user(userPrompt->userPrompt.text(salonSearchPrompt).
                        param("query",request.getQ())
                .param("lat",request.getLat())
                .param("long",request.getLng()))
                .call().entity(SalonSearchFilters.class);
        System.out.println(filters);
        Specification<Salon> salonSpecification = Specification.where(
                 SalonSpecifications.isVerified())
                .and(SalonSpecifications.isActive())
                .and(SalonSpecifications.hasArea(filters.getArea()));
        System.out.println(salonSpecification);
        return null;
    }

    public static class SalonSpecifications {

        public static Specification<Salon> isVerified() {
            return (root, query, cb) -> cb.equal(root.get("isVerified"), Boolean.TRUE);
        }

        public static Specification<Salon> isActive() {
            return (root, query, cb) -> cb.equal(root.get("isActive"), Boolean.TRUE);
        }

        public static Specification<Salon> hasParking() {
            return (root, query, cb) -> cb.equal(root.get("parkingAvailable"), Boolean.TRUE);
        }

        public static Specification<Salon> hasCity(String city) {
            return (root, query, cb) -> cb.equal(root.get("city"), city);
        }

        public static Specification<Salon> hasArea(String area) {
            return (root, query, cb) -> cb.equal(root.get("area"), area);
        }

        public static Specification<Salon> hasGenderType(String genderType) {
            return (root, query, cb) -> cb.equal(root.get("genderType"), genderType);
        }

    }

}
