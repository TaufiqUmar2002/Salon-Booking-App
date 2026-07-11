package com.umar.model;

import com.umar.AIAssistantServiceApplication;
import com.umar.payload.enums.aiAssistant.Role;
import com.umar.tools.AiAssistantToolMO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String messageId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private ChatSession chatSession;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String content;

    @ElementCollection
    @CollectionTable(name = "tool_called", joinColumns = @JoinColumn(name = "message_id"))
    private List<AiAssistantToolMO> toolCalled;

    private Integer responseTimeInMs;

    private Integer tokensUsed;



    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;




}
