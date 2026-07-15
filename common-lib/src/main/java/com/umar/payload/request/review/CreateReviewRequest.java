package com.umar.payload.request.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;
    @Size(min = 20,max = 200)
    private String body;
    private String title;
    @Size(max = 5)
    private List<String> mediaUrls;
}
