package com.umar.payload.request.review;

import com.umar.payload.enums.review.Tone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReplayReviewRequest {
    private Tone tone;
    private Boolean includeDiscount;
    private String  customContext;

}
