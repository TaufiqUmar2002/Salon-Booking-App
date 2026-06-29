package com.umar.booking_service.controller;

import com.umar.booking_service.serviceinterface.IBookingAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking/ai")
@RequiredArgsConstructor
public class BookingAiController {

    private final IBookingAiService bookingAiService;


}
