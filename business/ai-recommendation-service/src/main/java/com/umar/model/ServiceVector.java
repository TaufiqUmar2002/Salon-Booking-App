package com.umar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class ServiceVector {

    @Id
    private Long serviceId;
    private String serviceVector;
    private Long salonId;
    private BigDecimal price;
}
