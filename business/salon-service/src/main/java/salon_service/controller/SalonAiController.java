package salon_service.controller;

import com.umar.payload.request.salon.SalonSearchRequest;
import com.umar.payload.request.salon.ai.SalonAiSearchRequest;
import com.umar.payload.request.salon.ai.SalonGenerateDescriptionRequest;
import com.umar.payload.response.salon.ai.SalonAiInsightResponse;
import com.umar.payload.response.salon.ai.SalonAiSearchResponse;
import com.umar.payload.response.salon.ai.SalonGenerateDescriptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import salon_service.serviceinterface.ISalonAiService;

@RestController
@RequestMapping("/api/salon/ai")
@RequiredArgsConstructor
public class SalonAiController {

    private final ISalonAiService salonAiService;

    @PreAuthorize("hasRole('SALON_OWNER')")
    @PostMapping("/generate-description")
    public ResponseEntity<SalonGenerateDescriptionResponse> generateDescription(@Valid @RequestBody SalonGenerateDescriptionRequest request){
        SalonGenerateDescriptionResponse response = this.salonAiService.generateDescription(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<SalonAiSearchResponse> searchSalon(@Valid @RequestBody SalonAiSearchRequest request){
        SalonAiSearchResponse response = this.salonAiService.searchSalon(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('SALON_OWNER') or hasRole('ADMIN')")
    @GetMapping("/insights/{salonId}")
    public ResponseEntity<SalonAiInsightResponse> getInsights(@PathVariable("salonId") Long salonId){
        SalonAiInsightResponse response = this.salonAiService.getInsights(salonId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
