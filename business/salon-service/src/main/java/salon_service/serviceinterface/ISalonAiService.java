package salon_service.serviceinterface;

import com.umar.payload.request.salon.ai.SalonAiSearchRequest;
import com.umar.payload.request.salon.ai.SalonGenerateDescriptionRequest;
import com.umar.payload.response.salon.ai.SalonAiInsightResponse;
import com.umar.payload.response.salon.ai.SalonAiSearchResponse;
import com.umar.payload.response.salon.ai.SalonGenerateDescriptionResponse;

public interface ISalonAiService  {

    SalonGenerateDescriptionResponse generateDescription(SalonGenerateDescriptionRequest request);
    SalonAiInsightResponse getInsights(Long id);
    SalonAiSearchResponse searchSalon(SalonAiSearchRequest request);
}
