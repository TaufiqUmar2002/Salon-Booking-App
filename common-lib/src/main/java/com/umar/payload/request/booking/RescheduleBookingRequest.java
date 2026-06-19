package com.umar.payload.request.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RescheduleBookingRequest {
    private String  newSlotStartTime;
    private String reason;
}
