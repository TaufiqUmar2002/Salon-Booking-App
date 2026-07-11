package com.umar.payload.request.aiAssistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiAssistantChatRequest {

    private String message;
    private String sessionId;
    private Context context;
}
