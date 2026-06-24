package user_service.service;

import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.user.ai.OnwardSuggestionRequest;
import com.umar.payload.response.user.ai.RecommendationResponse;
import com.umar.payload.response.user.ai.SuggestionResponse;
import com.umar.payload.response.user.ai.UserPreferenceProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import user_service.model.User;
import user_service.model.UserPreference;
import user_service.repository.UserPreferenceRepository;
import user_service.repository.UserRepository;
import user_service.serviceInterface.IUserAiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserAiService implements IUserAiService {

    @Value("classpath:prompts/system/salon-system.st")
    private Resource systemPromptResource;
    @Value("classpath:prompts/user/salon-recommendation.st")
    private Resource userPromptResource;
    private final UserRepository userRepository;
    private final ChatClient chatClient;
    private final UserPreferenceRepository userPreferenceRepository;



    @Override
    public RecommendationResponse onboardSuggestion(OnwardSuggestionRequest request) {
        try{
            UserDetails userDetails = getCurrentLoggedUser();
            if (userDetails == null) {
                throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "USER_NOT_AUTHENTICATED",
                    "user.ai.serviceNotAvailable");
            }

            String userEmail = userDetails.getUsername();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiException(
                            HttpStatus.NOT_FOUND,
                            "USER_NOT_FOUND",
                            "user.notFound"));
            if (!Boolean.TRUE.equals(user.getOnboardingComplete())) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "ONBOARDING_NOT_COMPLETE",
                        "user.onboardingNotComplete");
            }
            RecommendationResponse response =  chatClient.prompt()
                    .system(systemSpec -> systemSpec.text(systemPromptResource))
                    .user(userSpec -> userSpec.text(userPromptResource)
                            .param("hairType",request.getHairType())
                            .param("skinType",request.getSkinType())
                            .param("interest",request.getInterests())
                            .param("budget",request.getBudgetRange()))
                    .call()
                    .entity(RecommendationResponse.class);
            List<UserPreference> userPreferenceList = new ArrayList<>();
            if(response!=null && response.getSuggestions()!=null){
                for(SuggestionResponse suggestionResponse:response.getSuggestions()){
                    UserPreference userPreference = new UserPreference();
                    userPreference.setCategoryName(suggestionResponse.getCategoryName());
                    userPreference.setReason(suggestionResponse.getReason());
                    userPreference.setEstimatedPrice(suggestionResponse.getEstimatedPrice());
                    userPreference.setBestTimeToBook(suggestionResponse.getBestTimeToBook());
                    userPreference.setUserId(user.getId());
                    userPreference.setRequest(request.toString());
                    userPreferenceList.add(userPreference);

                }
            }
            userPreferenceRepository.saveAll(userPreferenceList);
            return response;
        } catch (Exception ex) {
            if (ex.getMessage().contains("OpenAI API is unreachable or returned an error")) {
                throw new ApiException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "AI_UNAVAILABLE",
                        "user.ai.unavailable");
            } else {
                throw ex;
            }
        }
    }

    @Override
    public UserPreferenceProfileResponse userPreference(Long userId) {
        List<UserPreference> userPreferenceList = userPreferenceRepository.getUserPreferenceByUserId(userId).orElseThrow(
                ()-> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "USER_NOT_FOUND",
                        "user.notFound"));
        return chatClient.prompt().system(systemPromptResource).user(userPreferenceList.toString())
                .call()
                .entity(UserPreferenceProfileResponse.class);
    }

    private UserDetails getCurrentLoggedUser(){
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserDetails){
                return  (UserDetails) principal;
            }
        }
        return null;
    }




}
