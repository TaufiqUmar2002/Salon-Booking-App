package com.umar.events.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ServiceCreatedEvent {
    private Long serviceId;
    private Long salonId;
    private Long categoryId;
    private String name;
    private BigDecimal price;
    private Integer durationMinutes;
    private Boolean isActive;
    private LocalDateTime producedAt;
}
