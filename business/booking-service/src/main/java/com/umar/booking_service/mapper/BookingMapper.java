package com.umar.booking_service.mapper;

import com.umar.booking_service.model.Booking;
import com.umar.payload.response.booking.BookingResponse;
import com.umar.payload.response.booking.BookingResponseV1;
import com.umar.payload.response.booking.UserBookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookingId",source = "id")
    BookingResponse toResponse(Booking request);

    @Mapping(target = "bookingId",source = "id")
    BookingResponseV1 toResponseV1(Booking booking);

    UserBookingResponse.BookingSummary toUserBookingResponse(Booking booking);

}
