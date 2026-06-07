package com.umar.payload.request.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateServiceRequest {
    private String name;
    private Long categoryId;
    private Integer durationMinutes;
    private BigDecimal price;
    private String currency;
    private String description;
    private Integer bufferMinutes;
    private Integer maxCapacity;
    private BigDecimal discountedPrice;
    private Boolean requiresDeposit;
    private BigDecimal depositAmount;
    private Boolean isFeatured;
    private List<Integer> availableDays;
    private String availableFromTime;
    private String availableToTime;
    private List<Long> staffIds;
    private List<String> tags;
    private Integer displayOrder;



}
