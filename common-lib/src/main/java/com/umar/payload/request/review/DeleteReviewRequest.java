package com.umar.payload.request.review;

import com.umar.payload.enums.review.DeleteReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteReviewRequest {
    private DeleteReview reason;
    private String reasonNote;
}
