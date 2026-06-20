package com.umar.exchange;

import com.umar.payload.response.booking.BookingResponseV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BOOKING-SERVICE",url = "http://localhost:7006",configuration = FeignJwtInterceptor.class)
public interface BookingClient {

    @GetMapping("/api/booking/{id}")
    BookingResponseV1 getBookingById(@PathVariable Long id);
}
