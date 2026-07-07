package com.umar.booking_service.events;

import com.umar.events.booking.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventProducer {


    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishCreateBookingEvent(BookingCreatedEvent event){
        log.info("[BookingEventProducer][BookingEventProducer] {}",event);
        kafkaTemplate.send("booking.created", event);
    }

    public void sendCancelBookingEvent(CancelBookingEvent event){
        log.info("[BookingEventProducer][sendCancelBookingEvent] {}",event);
        kafkaTemplate.send("booking.cancel",event);
    }

    public void publishRescheduleBookingEvent(BookingRescheduledEvent event){
        log.info("[BookingEventProducer][publishRescheduleBookingEvent] {}",event);
        kafkaTemplate.send("booking.reschedule",event);
    }

    public void publishBookingCompletedEvent(BookingCompletedEvent event){
        log.info("[BookingEventProducer][publishBookingCompletedEvent] {}",event);
        kafkaTemplate.send("booking.completed",event);
    }

    public void publishBookingNoShowEvent(BookingNoShowEvent event){
        log.info("[BookingEventProducer][publishBookingNoShowEvent] {}",event);
        kafkaTemplate.send("booking.noShow",event);
    }
}
