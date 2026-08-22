package com.umar.service_offering.controller;

import com.umar.payload.request.services.ai.AiBulkDescribeRequest;
import com.umar.payload.request.services.ai.AiServiceDescribe;
import com.umar.payload.response.services.ai.*;
import com.umar.service_offering.serviceinterface.IServiceOfferingAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/ai")
@RequiredArgsConstructor
public class ServiceOfferingAiController {

    private final IServiceOfferingAiService aiService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/price-suggest/{id}")
    public ResponseEntity<AiPriceSuggestResponse> priceSuggest(@PathVariable Long id){
        AiPriceSuggestResponse response = this.aiService.priceSuggest(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/describe")
    public ResponseEntity<AiDescribeResponse> describe(@Valid @RequestBody AiServiceDescribe aiServiceDescribe){
        AiDescribeResponse describeResponse = this.aiService.describe(aiServiceDescribe);
        return ResponseEntity.ok(describeResponse);
    }

    @GetMapping("/upsell/{serviceId}")
    public ResponseEntity<AiServiceUpsellResponse> upsell(@PathVariable Long serviceId){
        AiServiceUpsellResponse upsellResponse = this.aiService.upsell(serviceId);
        return ResponseEntity.ok(upsellResponse);
    }

    @PostMapping("/bulk-describe/{salonId}")
    public ResponseEntity<AiBulDescribeResponse> bulkDescribe(@PathVariable Long salonId, @RequestBody(required = false) AiBulkDescribeRequest aiBulkDescribeRequest){
        AiBulDescribeResponse aiBulDescribeResponse  = this.aiService.bulkDescribe(salonId,aiBulkDescribeRequest);
        return ResponseEntity.ok(aiBulDescribeResponse);
    }

    @GetMapping("/performance/{salonId}")
    public ResponseEntity<AiServicePerformance> performance(@PathVariable Long salonId){
        AiServicePerformance aiServicePerformance = this.aiService.performance(salonId);
        return ResponseEntity.ok(aiServicePerformance);
    }
}
