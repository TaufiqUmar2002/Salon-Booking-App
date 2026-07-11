package com.umar.model;

import com.umar.payload.enums.aiAssistant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "chat_session")
@EntityListeners(AuditingEntityListener.class)
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer messageCount;

    private String conversationSummary;

    private List<String> detectedIntents;

    private List<String> toolsInvolved;

    private Integer bookingsCreated;

    private Integer cancellationsProcessed;

    private Boolean escalatedToHuman;

    @CreationTimestamp
    private LocalDateTime startedAt;

    @CreatedBy
    private String startedBy;

    @LastModifiedDate
    private LocalDateTime lastMessagedAt;

    @CreatedBy
    private String lastMessagedBy;

    private LocalDateTime completedAt;


    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;


}
