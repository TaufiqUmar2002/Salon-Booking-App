package com.umar.controller;

import com.umar.payload.request.aiAssistant.AiAssistantChatRequest;
import com.umar.payload.request.aiAssistant.EscalateChatRequest;
import com.umar.payload.request.aiAssistant.FeedbackRequest;
import com.umar.payload.response.aiAssistant.*;
import com.umar.serviceinterface.IAiAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ai-assistant")
public class AiAssistantController {

    private final IAiAssistantService aiAssistantService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/chat")
    public ResponseEntity<AiAssistantChatResponse> chat(@RequestBody AiAssistantChatRequest request) {
        AiAssistantChatResponse response = aiAssistantService.chat(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/history/{userId}")
    public ResponseEntity<AiAssistantChatHistoryResponse> getUserHistory(@PathVariable Long userId, @RequestParam String sessionId, @RequestParam String beforeMessageId, @RequestParam Integer limit){
        AiAssistantChatHistoryResponse response = aiAssistantService.getUserHistory(userId, sessionId, beforeMessageId, limit);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reset/{userId}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long userId){
        aiAssistantService.deleteChat(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chat/escalate")
    public ResponseEntity<EscalateChatResponse> escalateChat(@RequestBody EscalateChatRequest request) {
        EscalateChatResponse response = aiAssistantService.escalateChat(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat/feedback")
    public ResponseEntity<FeedbackResponse> feedback(@RequestBody FeedbackRequest request) {
        FeedbackResponse response = aiAssistantService.feedback(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sessions/{userId}")
    public ResponseEntity<ChatSessionResponse> getSessions(@PathVariable Long userId) {
        ChatSessionResponse sessions = aiAssistantService.getSessions(userId);
        return ResponseEntity.ok(sessions);
    }



}
