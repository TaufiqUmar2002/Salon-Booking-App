package com.umar.events.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationMetricsDto {
    private BigDecimal openRate;
    private BigDecimal clickRate;
    private BigDecimal optOutRate;
    private Integer avgOpenDelayMinutes;
}
