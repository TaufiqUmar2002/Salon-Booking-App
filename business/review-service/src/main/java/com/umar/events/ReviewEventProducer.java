package com.umar.events;

import com.umar.events.reviews.CreateReviewEventRequest;
import com.umar.payload.request.review.ReviewSentimentalScoreEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewEventProducer {

    public void publishCreateReviewEvent(CreateReviewEventRequest request){
        log.info("[ReviewEventProducer][publishCreateReviewEvent]");
    }

    public void publishReviewSentimentScoreEvent(ReviewSentimentalScoreEvent scoreEvent){
        log.info("[ReviewEventProducer][publishReviewSentimentScoreEvent]");
    }
}
