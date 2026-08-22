package com.umar.mapper;

import com.umar.model.Review;
import com.umar.payload.response.review.ReviewResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-22T21:00:14+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponse toResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse reviewResponse = new ReviewResponse();

        reviewResponse.setReviewId( review.getId() );
        reviewResponse.setBookingId( review.getBookingId() );
        reviewResponse.setSalonId( review.getSalonId() );
        reviewResponse.setRating( review.getRating() );
        reviewResponse.setBody( review.getBody() );
        reviewResponse.setTitle( review.getTitle() );
        reviewResponse.setServiceName( review.getServiceName() );
        reviewResponse.setSentimentLabel( review.getSentimentLabel() );
        reviewResponse.setIsSpam( review.getIsSpam() );
        reviewResponse.setIsVisible( review.getIsVisible() );
        reviewResponse.setCreatedAt( review.getCreatedAt() );

        return reviewResponse;
    }
}
