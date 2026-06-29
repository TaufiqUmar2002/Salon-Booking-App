package com.umar.payload.request.booking;

import com.umar.payload.enums.booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserBookingParamRequest {
    private BookingStatus status;
    private String from;
    private String to;
    private Integer page;
    private Integer size;
    private String sort;

}
