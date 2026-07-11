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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class SalonVector {

    @Id
    private Long salonId;
    private String salonVector;
    private Long categoryId;
    private String city;
    private Double averageRating;
    private Integer sentimentScore;
}
