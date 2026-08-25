package com.umar.payload.request.booking.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiBookingSuggestAttribute {
    private Long salonId;
    private Long serviceId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Integer count;
}
