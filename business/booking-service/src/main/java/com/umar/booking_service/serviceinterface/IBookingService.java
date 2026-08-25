package com.umar.booking_service.serviceinterface;

import com.umar.payload.request.booking.*;
import com.umar.payload.response.booking.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    BookingSummaryResponse salonSummary(Long salonId);
    UserBookingResponse getBookingByCategory(Long categoryId);
    List<LocalDateTime> generateAllPossibleSlots(LocalDate targetDate, String businessHoursStr, int durationMinutes);

}
