package com.umar.payment_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "promo_codes")
@EntityListeners(AuditingEntityListener.class)
public class PromoCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String promoCode;
    private String promoName;
    private String discountBasedOnFlag;
    private BigDecimal discountAmountOrRate;
    private String discountType;
    private LocalDateTime expiryDate;
    private Boolean isActive;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Boolean isPromoActiveAndNotExpired(){
        return isActive && expiryDate.isAfter(LocalDateTime.now());
    }

}
