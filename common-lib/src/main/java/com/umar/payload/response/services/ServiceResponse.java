package com.umar.payload.response.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ServiceResponse {
    private Long serviceId;
    private String name;
    private Long categoryId;
    private Integer durationMinutes;
    private Boolean isActive;
    private Boolean isFeatured;
    private Double averageRating;
    private Integer bookingCount;
    private String imageUrl;


}
