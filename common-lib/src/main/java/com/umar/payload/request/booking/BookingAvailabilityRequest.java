package com.umar.payload.request.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingAvailabilityRequest {

    @NotNull(message = "Salon ID is required")
    private Long salonId;
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    @NotNull(message = "Booking Date is required")
    private String date;
    private Long staffId;
}
