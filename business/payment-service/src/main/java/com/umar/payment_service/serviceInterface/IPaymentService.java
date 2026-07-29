package com.umar.payment_service.serviceInterface;

import com.umar.payload.request.payments.PaymentInitiationRequest;
import com.umar.payload.response.payments.PaymentInitiationResponse;

public interface IPaymentService {

    PaymentInitiationResponse initiatePayment(PaymentInitiationRequest request);
    void confirmPayment(String payload, String signature);
    void refundPayment(Long bookingId);
    void getPaymentHistoryByUser(Long userId);

}
