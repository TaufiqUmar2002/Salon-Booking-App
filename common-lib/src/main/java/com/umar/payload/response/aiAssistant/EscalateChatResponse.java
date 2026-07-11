package com.umar.payload.response.aiAssistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EscalateChatResponse {
    private String escalationId;
    private String status;
    private String estimatedResponseTime;
    private String confirmationMessage;
    private String aiHandoffSummary;



}
