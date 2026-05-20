package com.umar.payload.request.salon;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSummary {

    private String name;

    private Integer durationMinutes;

    private BigDecimal price;
}
