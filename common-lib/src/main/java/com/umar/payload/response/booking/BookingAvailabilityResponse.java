package com.umar.payload.response.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingAvailabilityResponse {
    private String date;
    private Long salonId;
    private Long serviceId;
    private Integer serviceDuration;
    private List<String> availableSlots;
    private Integer totalAvailable;
    private Boolean fullyBooked;
}
