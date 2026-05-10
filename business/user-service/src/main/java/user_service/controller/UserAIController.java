package user_service.controller;

import com.umar.payload.response.user.ai.RecommendationResponse;
import com.umar.payload.response.user.ai.UserPreferenceProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/ai")
public class UserAIController {


    /*Need to build with latest version current unsupported*/
    @PostMapping("/onboard-suggestions")
    @PreAuthorize(
            "hasRole('ADMIN') or #id == authentication.principal.id"
    )
    public ResponseEntity<RecommendationResponse> onboardSuggestion(){
        return null;
    }

    @GetMapping("/preferences/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserPreferenceProfileResponse> preferences(@PathVariable("id") Long id){
        return null;
    }


}
