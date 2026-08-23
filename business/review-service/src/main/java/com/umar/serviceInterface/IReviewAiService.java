package com.umar.serviceInterface;

import com.umar.payload.request.review.ReplayReviewRequest;
import com.umar.payload.request.review.ReviewSummaryResponse;
import com.umar.payload.response.review.ReviewReplyResponse;

public interface IReviewAiService {
    void sentimentAnalysis(Long review);
    ReviewReplyResponse replyGeneration(ReplayReviewRequest request, Long review);
    ReviewSummaryResponse getReviewSummary(Long reviewId, Boolean forceRefresh, Integer maxReviews);
}
