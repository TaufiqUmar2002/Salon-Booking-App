package com.umar.payload.response.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitiationResponse {
    private String paymentRef;
    private Long bookingId;
    private BigDecimal amount;
    private BigDecimal plateFormFee;
    private BigDecimal netAmount;
    private String discountApplied;
    private String currency;
    private String gatewayProvider;
    private String clientSecret;
    private String gatewayOrderId;
    private String fraudRiskBand;
    private String expiresAt;
    private String status;
}
