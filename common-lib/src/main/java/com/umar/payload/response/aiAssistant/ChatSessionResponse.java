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
public class ChatSessionResponse {


    private List<Sessions> sessions;
    private Long totalSessions;
    private Integer totalPages;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Sessions {
        private String sessionId;
        private String status;
        private String firstMessagePreview;
        private Integer messageCount;
        private Integer bookingCreated;
        private Integer cancellationsProcessed;
        private Boolean escalatedToHuman;
        private String startedAt;
        private String lastMessageAt;


    }

}
