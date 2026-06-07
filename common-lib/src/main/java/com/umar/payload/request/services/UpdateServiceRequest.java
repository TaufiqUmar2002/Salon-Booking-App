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
public class UpdateServiceRequest {
    private String name;
    private Long categoryId;
    private String description;
    private Integer durationMinutes;
    private Integer bufferMinutes;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer maxCapacity;
    private Boolean requiresDeposit;
    private BigDecimal depositAmount;
    private Boolean isFeatured;
    private List<Integer> availableDays;
    private String availableFromTime;
    private String availableToTime;
    private List<Long> staffIds;
    private List<String> tags;
    private String imageUrls;
    private Integer displayOrder;
}
