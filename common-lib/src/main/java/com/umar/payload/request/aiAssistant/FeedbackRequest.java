package com.umar.payload.request.aiAssistant;


import com.umar.payload.enums.aiAssistant.FeedbackReason;
import com.umar.payload.enums.aiAssistant.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackRequest {
    private String sessionId;
    private String messageId;
    private Rating rating;
    private FeedbackReason feedbackReason;
    private String feedBackNote;
}
