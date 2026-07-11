package com.umar.payload.response.aiAssistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackResponse {
    private String feedbackId;
    private String messageId;
    private String rating;
    private String message;

}
