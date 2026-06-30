package com.umar.payload.request.salon;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSummary {

    private Long serviceId;
    @Size(max = 120, message = "Service name must be less than 120 characters")
    private String serviceName;

    @Min(value = 5, message = "Duration must be at least 1 minute")
    @Max(value = 480,message = "Duration must be less than 8 hours")
    private Integer durationMinutes;

    @Positive
    private BigDecimal price;
}
