package com.umar.payload.request.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    private Long bookingId;
    private Long salonId;
    private Integer rating;
    private String body;
    private String title;
    private List<String> mediaUrls;
}
