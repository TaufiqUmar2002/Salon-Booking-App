package com.umar.payload.request.review;

import com.umar.payload.enums.review.SentimentLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewSentimentalScoreEvent {

    private Long reviewId;
    private Long salonId;
    private SentimentLabel sentiment;
    private Double sentimentScore;
    private List<String> themes;
    private LocalDateTime scoreAt;

}
