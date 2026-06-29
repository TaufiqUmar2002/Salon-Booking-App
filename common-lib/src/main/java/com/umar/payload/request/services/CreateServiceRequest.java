package com.umar.payload.request.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
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
public class CreateServiceRequest {

    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    @Min(value = 5, message = "Duration must be at least 5 minutes")
    @Max(value = 480, message = "Duration must be at most 480 minutes")
    private Integer durationMinutes;
    @Digits(integer = 10, fraction = 2, message = "Price can have max 2 decimal places")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency should be a valid ISO 4217 code")
    private String currency;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @Min(value = 0,message = "Buffer minutes must be between 0 and 60")
    @Max(value = 60,message = "Buffer minutes must be between 0 and 60")
    private Integer bufferMinutes;

    @Min(value = 1,message = "Min capacity must be at least 1")
    private Integer maxCapacity;
    private BigDecimal discountedPrice;
    private Boolean requiresDeposit;
    private BigDecimal depositAmount;
    private Boolean isFeatured;
    private List<Integer> availableDays;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime availableFromTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime availableToTime;
    private List<Long> staffIds;

    @Size(max = 10, message = "Max 10 tags")
    @Valid
    private List<String> tags;
    private Integer displayOrder;



}
