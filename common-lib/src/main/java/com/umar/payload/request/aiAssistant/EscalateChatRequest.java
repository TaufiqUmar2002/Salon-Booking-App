package com.umar.payload.request.aiAssistant;

import com.umar.payload.enums.aiAssistant.Reason;
import com.umar.payload.enums.aiAssistant.Urgency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EscalateChatRequest {
    private String sessionId;
    private Reason reason;
    private Urgency urgency;
    private String  userNote;
}
