package com.umar.booking_service.controller;

import com.umar.booking_service.serviceinterface.IBookingService;
import com.umar.payload.request.booking.*;
import com.umar.payload.response.booking.BookingAvailabilityResponse;
import com.umar.payload.response.booking.BookingResponse;
import com.umar.payload.response.booking.BookingResponseV1;
import com.umar.payload.response.booking.UserBookingResponse;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/booking")
@RequiredArgsConstructor
@RestController
public class BookingController {

    private final IBookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request){
        BookingResponse booking =bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/availability")
    public ResponseEntity<BookingAvailabilityResponse> getAvailability(@ModelAttribute BookingAvailabilityRequest request){
        BookingAvailabilityResponse response = this.bookingService.getAvailableSlot(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<BookingResponseV1> getBookingById(@PathVariable Long id){
        BookingResponseV1 responseV1 = this.bookingService.getBookingById(id);
        return ResponseEntity.ok(responseV1);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserBookingResponse> getBookingByUserId(@PathVariable Long userId, @ModelAttribute UserBookingParamRequest request){
        UserBookingResponse response = this.bookingService.getBookingByUser(userId,request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<UserBookingResponse> getBookingBySalonId(@PathVariable Long salonId, @ModelAttribute SalonBookingParamRequest request){
        UserBookingResponse response = this.bookingService.getBookingBySalonId(salonId,request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id, CancelBookingRequest cancelBookingRequest){
        BookingResponse response = this.bookingService.cancelBooking(cancelBookingRequest.getReason(),id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<BookingResponse> rescheduleBooking(@PathVariable Long id,RescheduleBookingRequest request){
        BookingResponse response = this.bookingService.rescheduleBooking(id,request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}/complete")
    public ResponseEntity<BookingResponse> completeBooking(@PathVariable Long id){
        BookingResponse response = this.bookingService.completeBooking(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}/noShow")
    public ResponseEntity<BookingResponse> noShowBooking(@PathVariable Long id){
        BookingResponse response = this.bookingService.noShowBooking(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/salon/{salonId}/summary")
    public ResponseEntity<BookingResponse> salonSummary(@PathVariable Long salonId){
        BookingResponse Response = this.bookingService.salonSummary(salonId);
        return ResponseEntity.ok(Response);
    }

}
