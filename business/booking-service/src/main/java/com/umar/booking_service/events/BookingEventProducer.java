package com.umar.booking_service.events;

import com.umar.events.booking.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventProducer {

    public void publishCreateBookingEvent(BookingCreatedEvent event){
        System.out.println("...");
    }
}
