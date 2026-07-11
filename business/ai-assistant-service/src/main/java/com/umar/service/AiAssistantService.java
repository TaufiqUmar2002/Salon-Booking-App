package com.umar.service;

import com.umar.payload.request.aiAssistant.AiAssistantChatRequest;
import com.umar.payload.request.aiAssistant.EscalateChatRequest;
import com.umar.payload.request.aiAssistant.FeedbackRequest;
import com.umar.payload.response.aiAssistant.*;
import com.umar.serviceinterface.IAiAssistantService;
import org.springframework.stereotype.Service;

@Service
public class AiAssistantService implements IAiAssistantService {

    @Override
    public AiAssistantChatResponse chat(AiAssistantChatRequest request) {
        return null;
    }

    @Override
    public AiAssistantChatHistoryResponse getUserHistory(Long userId, String sessionId, String beforeMessageId, Integer limit) {
        return null;
    }

    @Override
    public void deleteChat(Long userId) {

    }

    @Override
    public EscalateChatResponse escalateChat(EscalateChatRequest request) {
        return null;
    }

    @Override
    public FeedbackResponse feedback(FeedbackRequest request) {
        return null;
    }

    @Override
    public ChatSessionResponse getSessions(Long userId) {
        return null;
    }
}
