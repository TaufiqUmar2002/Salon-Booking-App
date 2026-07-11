package com.umar.payload.request.aiAssistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Context {

    private Integer currentPage;
    private Long currentSalonId;
    private Long currentBookingId;
}
