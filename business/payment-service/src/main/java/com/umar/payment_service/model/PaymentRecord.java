package com.umar.payment_service.model;

import com.umar.payload.constants.PaymentMethod;
import com.umar.payload.constants.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PAYMENT_RECORD")
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String paymentRef;
    private Long bookingId;
    private Long userId;
    private Long salonId;
    private BigDecimal amount;
    private BigDecimal platformFee;
    private BigDecimal netAmount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String gatewayProvider;
    private String gatewayPaymentId;
    private String gatewayOrderId;
    private String gatewayStatus;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String cardLastFour;
    private Double fraudScore;
    private String fraudFlagReason;
    private BigDecimal depositPaid;
    private BigDecimal refundAmount;
    private String refundGatewayId;
    private LocalDateTime refundedAt;
    private String receiptUrl;
    @ElementCollection
    private List<String> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
