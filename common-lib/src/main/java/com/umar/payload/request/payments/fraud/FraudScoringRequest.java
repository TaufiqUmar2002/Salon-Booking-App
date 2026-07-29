package com.umar.payload.request.payments.fraud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FraudScoringRequest {
    private Long userId;
    private BigDecimal amount;
    private String currency;
    private String deviceFingerprint;
    private String ipAddress;
    private String paymentMethod;
    private Long bookingId;
    private LocalDateTime timestamp;
}
