package com.umar.serviceinterface;

import com.umar.payload.request.aiAssistant.AiAssistantChatRequest;
import com.umar.payload.request.aiAssistant.EscalateChatRequest;
import com.umar.payload.request.aiAssistant.FeedbackRequest;
import com.umar.payload.response.aiAssistant.*;

public interface IAiAssistantService {

    AiAssistantChatResponse chat(AiAssistantChatRequest request);
    AiAssistantChatHistoryResponse getUserHistory(Long userId, String sessionId, String beforeMessageId, Integer limit);
    void deleteChat(Long userId);
    EscalateChatResponse escalateChat(EscalateChatRequest request);
    FeedbackResponse feedback(FeedbackRequest request);
    ChatSessionResponse getSessions(Long userId);
}
