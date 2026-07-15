package com.umar.controller;

import com.umar.payload.request.review.CreateReviewRequest;
import com.umar.payload.request.review.DeleteReviewRequest;
import com.umar.payload.response.review.ReviewResponse;
import com.umar.payload.response.review.ReviewResponseList;
import com.umar.serviceInterface.IReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final IReviewService reviewService;


    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid@RequestBody CreateReviewRequest request){
        ReviewResponse response = this.reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<ReviewResponseList> getReviewsBySalon(@PathVariable Long salonId){
        ReviewResponseList reviewResponseList = this.reviewService.getReviewsBySalon(salonId);
        return ResponseEntity.ok(reviewResponseList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id,@RequestBody DeleteReviewRequest request){
        this.reviewService.deleteReview(id,request);
        return ResponseEntity.noContent().build();
    }
}
