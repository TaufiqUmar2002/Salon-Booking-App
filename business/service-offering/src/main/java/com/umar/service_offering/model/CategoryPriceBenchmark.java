package com.umar.service_offering.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "category_price_benchmark")
@EntityListeners(AuditingEntityListener.class)
public class CategoryPriceBenchmark {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Double cancellationRate;

    private Integer avgLeadTime;

    private Integer demandScore;

    private BigDecimal competitorAvgPrice;

    private String city;

    private Long categoryId;


}
