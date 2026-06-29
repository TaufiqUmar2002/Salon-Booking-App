package com.umar.service;

import com.umar.events.ReviewEventProducer;
import com.umar.events.reviews.CreateReviewEventRequest;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.exchange.BookingClient;
import com.umar.mapper.ReviewMapper;
import com.umar.model.Review;
import com.umar.model.ReviewAuditLog;
import com.umar.payload.enums.booking.BookingStatus;
import com.umar.payload.request.review.CreateReviewRequest;
import com.umar.payload.request.review.DeleteReviewRequest;
import com.umar.payload.response.booking.BookingResponseV1;
import com.umar.payload.response.review.ReviewResponse;
import com.umar.repository.ReviewAuditRepository;
import com.umar.repository.ReviewRepository;
import com.umar.serviceInterface.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository repository;
    private final BookingClient bookingClient;
    private final ReviewEventProducer eventProducer;
    private final ReviewMapper mapper;
    private final ReviewAuditRepository auditRepository;

    @Override
    public ReviewResponse createReview(CreateReviewRequest request) {
        BookingResponseV1 bookingResponse = bookingClient.getBookingById(request.getBookingId());
        if(!bookingResponse.getSalonId().equals(request.getSalonId())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"SALON_MISMATCH","The booking does not belong to this salon");
        }
        if(!bookingResponse.getStatus().equals(BookingStatus.COMPLETED)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"BOOKING_NOT_COMPLETED","Reviews can only be submitted for completed appointments");
        }
        if(bookingResponse.getUserId().equals(387834L)){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","'You can only review your own bookings");
        }
        Integer reviewExistsWithGivenBooking = repository.getReviewByBookingId(request.getBookingId());
        if(reviewExistsWithGivenBooking>0){
            throw new ApiException(HttpStatus.CONFLICT,"REVIEW_EXISTS","review.alreadyExists");
        }
        Review review = Review.builder()
                .salonId(request.getSalonId())
                .isSpam(false)
                .createdAt(LocalDateTime.now())
                .bookingId(request.getBookingId())
                .serviceName(bookingResponse.getServiceId().toString())
                .build();
        Review persistReview = repository.save(review);
        CreateReviewEventRequest eventRequest = CreateReviewEventRequest.builder()
                .reviewId(persistReview.getId())
                .salonId(request.getSalonId())
                .booKingId(request.getBookingId())
                .producedAt(LocalDateTime.now())
                .build();
        eventProducer.publishCreateReviewEvent(eventRequest);
        return mapper.toResponse(persistReview)  ;
    }

    @Override
    public void deleteReview(Long id, DeleteReviewRequest request) {
        Review review = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"REVIEW_NOT_FOUND","review.notFound"));
        if(review.getIsAdminDeleted().equals(Boolean.TRUE)){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","Review already deleted");
        }
        review.setIsAdminDeleted(true);
        review.setIsVisible(false);
        review.setUpdatedAt(LocalDateTime.now());
        repository.save(review);
        ReviewAuditLog reviewAuditLog = ReviewAuditLog.builder()
                .reason(request.getReason())
                .performedAt(LocalDateTime.now())
                .reviewId(review.getId())
                .reasonNote(request.getReasonNote())
                .build();
        auditRepository.save(reviewAuditLog);
    }
}
