package user_service.serviceInterface;

import com.umar.payload.request.user.ai.OnwardSuggestionRequest;
import com.umar.payload.response.user.ai.RecommendationResponse;
import com.umar.payload.response.user.ai.UserPreferenceProfileResponse;

public interface IUserAiService {
    RecommendationResponse onboardSuggestion(OnwardSuggestionRequest request);
    UserPreferenceProfileResponse userPreference(Long id);
}
