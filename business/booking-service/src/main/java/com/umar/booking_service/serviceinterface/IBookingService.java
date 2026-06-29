package com.umar.booking_service.serviceinterface;

import com.umar.payload.request.booking.*;
import com.umar.payload.response.booking.BookingAvailabilityResponse;
import com.umar.payload.response.booking.BookingResponse;
import com.umar.payload.response.booking.BookingResponseV1;
import com.umar.payload.response.booking.UserBookingResponse;

public interface IBookingService {

    BookingResponse createBooking(BookingRequest request);
    BookingAvailabilityResponse getAvailableSlot(BookingAvailabilityRequest request);
    BookingResponseV1 getBookingById(Long id);
    UserBookingResponse getBookingByUser(Long id, UserBookingParamRequest request);
    UserBookingResponse getBookingBySalonId(Long id, SalonBookingParamRequest request);
    BookingResponse cancelBooking(String reason,Long id);
    BookingResponse rescheduleBooking(Long id, RescheduleBookingRequest request);
    BookingResponse completeBooking(Long id);
    BookingResponse noShowBooking(Long id);
    BookingResponse salonSummary(Long salonId);
    UserBookingResponse getBookingByCategory(Long categoryId);

}
