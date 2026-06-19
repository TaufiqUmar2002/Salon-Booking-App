package com.umar.payload.response.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private String status;
    private Long salonId;
    private Long serviceId;
    private String slotStartTime;
    private String slotEndTime;
    private String totalPrice;
    private String currency;
    private String createdAt;
}
