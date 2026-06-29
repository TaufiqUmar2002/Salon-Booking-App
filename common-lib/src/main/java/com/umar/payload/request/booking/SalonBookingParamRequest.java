package com.umar.payload.request.booking;

import com.umar.payload.enums.booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalonBookingParamRequest {
    private String date;
    private String from;
    private String to;
    private BookingStatus status;
    private Long staffId;
    private Integer page;
    private Integer size;
}
