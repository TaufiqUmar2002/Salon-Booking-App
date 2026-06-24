package user_service.controller;

import com.umar.payload.request.user.ai.OnwardSuggestionRequest;
import com.umar.payload.response.user.ai.RecommendationResponse;
import com.umar.payload.response.user.ai.UserPreferenceProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import user_service.serviceInterface.IUserAiService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/ai")
public class UserAIController {

    private final IUserAiService userAiService;


    @PostMapping("/onboard-suggestions")
    @PreAuthorize(
            "hasRole('ADMIN') or #id == authentication.principal.id"
    )
    public ResponseEntity<RecommendationResponse> onboardSuggestion(@Valid @RequestBody OnwardSuggestionRequest request){
        RecommendationResponse response = this.userAiService.onboardSuggestion(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/preferences/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserPreferenceProfileResponse> preferences(@PathVariable("id") Long id){
        UserPreferenceProfileResponse response = this.userAiService.userPreference(id);
        return ResponseEntity.ok(response);
    }


}
