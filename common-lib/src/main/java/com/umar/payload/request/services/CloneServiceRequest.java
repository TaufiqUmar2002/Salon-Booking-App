package com.umar.payload.request.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CloneServiceRequest {

    private String name;
    private BigDecimal price;
    private Integer durationMinutes;
    private String description;
    private Long categoryId;
    private Boolean isFeatured;

}
