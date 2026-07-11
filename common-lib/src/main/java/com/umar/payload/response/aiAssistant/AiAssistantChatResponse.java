package com.umar.payload.response.aiAssistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiAssistantChatResponse {
    private String sessionId;
    private String messageId;
    private String response;
    private List<String> toolsInvoked;
    private PendingAction pendingAction;
    private String confirmationToken;
    private List<String> suggestedReplies;
    private Integer responseTimeMs;
    private Integer messageCount;
}
