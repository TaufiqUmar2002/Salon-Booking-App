package com.umar.payload.request.services;

import jakarta.validation.constraints.*;
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

    @NotEmpty(message = "name is required")
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "price is required")
    @Positive(message = "price must be positive")
    private BigDecimal price;
    @Min(value = 5, message = "Duration must be at least 5 minutes")
    @Max(value = 480, message = "Duration must be at most 480 minutes")
    private Integer durationMinutes;
    @NotNull(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;
    @Positive(message = "Category id must be positive")
    private Long categoryId;
    private Boolean isFeatured;

}
