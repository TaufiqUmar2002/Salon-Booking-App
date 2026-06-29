package com.umar.payload.response.review;

import com.umar.payload.enums.review.SentimentLabel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private Long bookingId;
    private Long salonId;
    private Integer rating;
    private String body;
    private String title;
    private String serviceName;
    private SentimentLabel sentimentLabel;
    private Boolean isSpam;
    private Boolean isVisible;
    private LocalDateTime createdAt;
    private String message;
}
