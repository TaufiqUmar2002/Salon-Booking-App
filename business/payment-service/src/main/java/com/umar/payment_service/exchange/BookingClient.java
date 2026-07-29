package com.umar.payment_service.exchange;

import com.umar.payload.response.booking.BookingResponseV1;
import com.umar.payload.response.booking.UserBookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BOOKING-SERVICE",url = "http://localhost:5009",configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface BookingClient {

    @GetMapping("/booking/{id}")
    BookingResponseV1 getBookingById(@PathVariable("id") Long id);

}
