package com.umar.payment_service.controller;

import com.umar.payload.request.payments.PaymentInitiationRequest;
import com.umar.payload.response.payments.PaymentInitiationResponse;
import com.umar.payment_service.serviceInterface.IPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentInitiationResponse> initiatePayment(@Valid @RequestBody PaymentInitiationRequest request){
        return null;
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPayment(){
        return null;
    }

    @PostMapping("/refund/{bookingId}")
    public ResponseEntity<Void> bookPayment(@PathVariable Long bookingId){
        return null;
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<Void> getPaymentHistoryByUser(@PathVariable Long userId){
        return null;
    }

}
