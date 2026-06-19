package com.umar.payload.request.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingAvailabilityRequest {

    private Long salonId;
    private Long serviceId;
    private String date;
    private Long staffId;
}
