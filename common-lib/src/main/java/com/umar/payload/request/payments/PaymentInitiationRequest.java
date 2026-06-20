package com.umar.payload.request.payments;

import com.umar.payload.constants.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitiationRequest {
    private Long bookingId;
    private PaymentMethod paymentMethod;
    private String currency;
    private String gatewayProvider;
    private String promoCode;
    private String deviceFingerprint;
    private String ipAddress;
}
