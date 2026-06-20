package com.umar.serviceInterface;

import com.umar.payload.request.review.CreateReviewRequest;
import com.umar.payload.request.review.DeleteReviewRequest;
import com.umar.payload.response.review.ReviewResponse;

public interface IReviewService {

    ReviewResponse createReview(CreateReviewRequest request);
    void deleteReview(Long id, DeleteReviewRequest request);
}
