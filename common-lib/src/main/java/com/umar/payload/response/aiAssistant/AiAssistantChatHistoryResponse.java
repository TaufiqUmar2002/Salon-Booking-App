package com.umar.payload.response.aiAssistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiAssistantChatHistoryResponse {

    private String sessionId;
    private String sessionStatus;
    private List<Messages> messages;
    private Boolean hasMore;
    private SessionSummary sessionSummary;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Messages{
        private String messageId;
        private String role;
        private String content;
        private String toolCalled;
        private LocalDateTime createdAt;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class SessionSummary{
        private Long messageCount;
        private LocalDateTime bookingsCreated;
        private Boolean cancellationsProcessed;
        private LocalDateTime startedAt;
        private LocalDateTime lastMessageAt;
    }
}
