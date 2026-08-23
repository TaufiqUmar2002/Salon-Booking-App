package com.umar.controller;

import com.umar.payload.request.review.ReplayReviewRequest;
import com.umar.payload.request.review.ReviewSummaryResponse;
import com.umar.payload.response.review.ReviewReplyResponse;
import com.umar.payload.response.review.ReviewResponse;
import com.umar.serviceInterface.IReviewAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review/ai")
@RequiredArgsConstructor
public class ReviewAiController {

    private final IReviewAiService reviewAiService;

    @PostMapping("/sentiment/{reviewId}")
    public ResponseEntity<Void> sentimentAnalysis(@PathVariable Long reviewId) {
        reviewAiService.sentimentAnalysis(reviewId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALON_OWNER')")
    @PostMapping("/replay/{reviewId}")
    public ResponseEntity<ReviewReplyResponse> replyGeneration(@RequestBody ReplayReviewRequest request, @PathVariable Long reviewId) {
        ReviewReplyResponse replyResponse = reviewAiService.replyGeneration(request,reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(replyResponse);
    }

    @GetMapping("/summery/{reviewId}")
    public ResponseEntity<ReviewSummaryResponse> getReviewSummery(@PathVariable Long reviewId, @PathVariable Boolean forceRefresh, @PathVariable Integer maxReviews){
        ReviewSummaryResponse response = this.reviewAiService.getReviewSummary(reviewId,forceRefresh,maxReviews);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
