package com.umar.payment_service.model;

import com.umar.payload.constants.Decision;
import com.umar.payload.constants.ReviewOutcome;
import com.umar.payload.constants.SignalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "FRAUD_SIGNAL")
public class FraudSignal {

    @Id
    private Long id;
    private Long userId;
    private String paymentRef;
    private Double fraudScore;
    @Enumerated(EnumType.STRING)
    private SignalType signalType;
    @Enumerated(EnumType.STRING)
    private Decision decision;
    private Long reviewedId;
    @Enumerated(EnumType.STRING)
    private ReviewOutcome reviewOutcome;
    private LocalDateTime createdAt;
}
