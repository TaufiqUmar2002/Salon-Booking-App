package com.umar.payload.request.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkUpdateServiceRequest {

    @NotEmpty(message = "Updates list cannot be empty")
    @Size(max = 50, message = "Maximum 50 updates allowed per request")
    @Valid
    private List<ServiceUpdateItem> updates;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ServiceUpdateItem {

        @NotNull(message = "Service ID is required")
        private Long serviceId;

        @DecimalMin(value = "0.0", inclusive = false,
                message = "Price must be greater than 0")
        @Digits(integer = 10, fraction = 2,
                message = "Price can have max 2 decimal places")
        private BigDecimal price;

        @Digits(integer = 10, fraction = 2,
                message = "Discounted price can have max 2 decimal places")
        private BigDecimal discountedPrice;

        private Boolean isFeatured;

        private List<DayOfWeek> availableDays;

        private Boolean isActive;
    }
}
