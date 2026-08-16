package com.umar.model;

import com.umar.payload.enums.notification.Channel;
import com.umar.payload.enums.notification.NotificationTypes;
import com.umar.payload.enums.notification.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventId;
    @Enumerated(EnumType.STRING)
    private NotificationTypes notificationType;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Channel channel;
    private String templateId;
    private String subject;
    private String bodyPreview;
    private String recipientAddress;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String suppressionReason;
    private String deliveryProvider;
    private String providerMessageId;
    private Integer retryCount;
    private Boolean aiComposed;
    private Boolean sendTimeOptimised;
    private LocalDateTime scheduledFor;
    private LocalDateTime deliveredAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreatedBy
    public String createdBy;
}
