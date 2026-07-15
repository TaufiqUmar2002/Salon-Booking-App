package com.umar.payload.response.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewResponseList {

    private List<ReviewResponse> reviewResponseList;
    private Long totalReviews;
    private Integer totalPages;
    private SentimentSummary sentimentSummary;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class SentimentSummary{
        private String positive;
        private String negative;
        private String neutral;
    }
}
