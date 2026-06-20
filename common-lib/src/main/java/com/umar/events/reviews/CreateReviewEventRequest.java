package com.umar.events.reviews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReviewEventRequest {
    private Long reviewId;
    private Long booKingId;
    private Long salonId;
    private Long userId;
    private Integer rating;
    private String body;
    private LocalDateTime producedAt;
}
