package com.umar.events;

import com.umar.events.reviews.CreateReviewEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewEventProducer {

    public void publishCreateReviewEvent(CreateReviewEventRequest request){
        log.info("[ReviewEventProducer][publishCreateReviewEvent]");
    }
}
