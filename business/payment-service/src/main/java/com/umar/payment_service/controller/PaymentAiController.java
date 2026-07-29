package com.umar.payment_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/")
@RequiredArgsConstructor
public class PaymentAiController {


    @PostMapping("/fraud-check")
    public ResponseEntity<Void> fraudCheck() {
        return null;
    }

    @GetMapping("/insights/{userId}")
    public ResponseEntity<Void> insights(@PathVariable Long userId) {
        return null;
    }

    @GetMapping("/discount-suggest/{userId}")
    public ResponseEntity<Void> discountSuggest(@PathVariable Long userId) {
        return null;
    }

}
