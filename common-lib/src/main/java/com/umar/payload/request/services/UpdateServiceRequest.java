package com.umar.payload.request.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateServiceRequest {
    private String name;
    private Long categoryId;
    @NotNull(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;
    @Min(value = 5, message = "Duration must be at least 5 minutes")
    @Max(value = 480, message = "Duration must be at most 480 minutes")
    private Integer durationMinutes;
    @Min(value = 0,message = "Buffer minutes must be between 0 and 60")
    @Max(value = 60,message = "Buffer minutes must be between 0 and 60")
    private Integer bufferMinutes;
    @Digits(integer = 10, fraction = 2, message = "Price can have max 2 decimal places")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer maxCapacity;
    private Boolean requiresDeposit;
    @Positive
    private BigDecimal depositAmount;
    private Boolean isFeatured;
    private List<Integer> availableDays;
    @NotNull(message = "Available from time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime availableFromTime;
    @NotNull(message = "Available to time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime availableToTime;
    private List<Long> staffIds;
    private List<String> tags;
    private String imageUrls;

    private Integer displayOrder;
}
