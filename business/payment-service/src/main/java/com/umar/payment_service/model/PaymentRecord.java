package com.umar.payment_service.model;

import com.umar.payload.enums.payment.PaymentMethod;
import com.umar.payload.enums.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PAYMENT_RECORD")
@EntityListeners(AuditingEntityListener.class)
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

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}
