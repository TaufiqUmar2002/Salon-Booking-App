package com.umar.service;

import com.umar.events.ReviewEventProducer;
import com.umar.events.reviews.CreateReviewEventRequest;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.exchange.BookingClient;
import com.umar.exchange.SalonClient;
import com.umar.exchange.ServiceClient;
import com.umar.exchange.UserClient;
import com.umar.mapper.ReviewMapper;
import com.umar.model.Review;
import com.umar.model.ReviewAuditLog;
import com.umar.payload.enums.booking.BookingStatus;
import com.umar.payload.request.review.CreateReviewRequest;
import com.umar.payload.request.review.DeleteReviewRequest;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.booking.BookingResponseV1;
import com.umar.payload.response.review.ReviewResponse;
import com.umar.payload.response.review.ReviewResponseList;
import com.umar.payload.response.salon.SalonResponse;
import com.umar.payload.response.salon.SalonResponseList;
import com.umar.payload.response.salon.SalonResponseV1;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.repository.ReviewAuditRepository;
import com.umar.repository.ReviewRepository;
import com.umar.serviceInterface.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository repository;
    private final BookingClient bookingClient;
    private final ReviewEventProducer eventProducer;
    private final ReviewMapper mapper;
    private final ReviewAuditRepository auditRepository;
    private final Executor executor;
    private final UserClient userClient;
    private final ServiceClient serviceClient;
    private final SalonClient salonClient;

    @Override
    public ReviewResponse createReview(CreateReviewRequest request) {
        BookingResponseV1 bookingResponse = bookingClient.getBookingById(request.getBookingId());
        UserValidateResponse response = this.userClient.getUserValidation();
        if(!bookingResponse.getSalonId().equals(request.getSalonId())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"SALON_MISMATCH","review.salonMismatch");
        }
        if(!bookingResponse.getStatus().equals(BookingStatus.COMPLETED)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"BOOKING_NOT_COMPLETED","review.bookingNotCompleted");
        }
        if(bookingResponse.getUserId().equals(response.getUserId())){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","review.youCanOnlyReviewYourOwnBookings");
        }
        Integer reviewExistsWithGivenBooking = repository.getReviewByBookingId(request.getBookingId());
        if(reviewExistsWithGivenBooking>0){
            throw new ApiException(HttpStatus.CONFLICT,"REVIEW_EXISTS","review.alreadyExists");
        }
        ServiceResponse serviceResponse = this.serviceClient.getServiceById(request.getSalonId());
        Review review = Review.builder()
                .salonId(request.getSalonId())
                .isSpam(false)
                .bookingId(request.getBookingId())
                .serviceName(serviceResponse.getName())
                .build();
        Review persistReview = repository.save(review);
        executor.execute(() -> {
            CreateReviewEventRequest eventRequest = CreateReviewEventRequest.builder()
                    .reviewId(persistReview.getId())
                    .salonId(request.getSalonId())
                    .booKingId(request.getBookingId())
                    .producedAt(LocalDateTime.now())
                    .build();
            eventProducer.publishCreateReviewEvent(eventRequest);
        });
        return mapper.toResponse(persistReview)  ;
    }

    @Override
    public void deleteReview(Long id, DeleteReviewRequest request) {
        Review review = this.repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"REVIEW_NOT_FOUND","review.notFound"));
        if(review.getIsAdminDeleted().equals(Boolean.TRUE)){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","review.alreadyDeleted");
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

    @Override
    public ReviewResponseList getReviewsBySalon(Long salonId) {
        List<Review> review = this.repository.findBySalonId(salonId);
        List<ReviewResponse> reviewResponseListData = review.stream().map(mapper::toResponse
        ).toList();
        return ReviewResponseList.builder()
                .reviewResponseList(
                        reviewResponseListData
                )
                .build();
    }
}
