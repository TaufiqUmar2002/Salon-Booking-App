package com.umar.payload.request.booking.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiBookingSuggestAttribute {
    private Long salonId;
    private Long serviceId;
    private String fromDate;
    private String toDate;
    private Integer count;
}
