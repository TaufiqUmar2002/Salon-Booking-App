package com.umar.service_offering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/ai")
@RequiredArgsConstructor
public class ServiceOfferingAiController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/price-suggest/{id}")
    public ResponseEntity<Void> priceSuggest(@PathVariable Long id){
        return null;
    }

    @PostMapping("/describe")
    public ResponseEntity<Void> describe(){
        return null;
    }

    @GetMapping("/upsell/{serviceId}")
    public ResponseEntity<Void> upsell(@PathVariable Long serviceId){
        return null;
    }

    @PostMapping("/bulk-describe")
    public ResponseEntity<Void> bulkDescribe(){
        return null;
    }

    @PostMapping("/bulk-upsell")
    public ResponseEntity<Void> bulkUpsell(){
        return null;
    }

    @GetMapping("/performance/{salonId}")
    public ResponseEntity<Void> performance(@PathVariable Long salonId){
        return null;
    }
}
