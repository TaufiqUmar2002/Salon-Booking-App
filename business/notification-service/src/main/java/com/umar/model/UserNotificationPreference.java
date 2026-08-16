package com.umar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserNotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Boolean emailEnabled;
    private Boolean smsEnabled;
    private Boolean pushEnabled;
    private Boolean inAppEnabled;
    private Boolean bookingConfirmations;
    private Boolean appointmentReminders;
    private  String reminderLeadHours;
    private Boolean promotionalEmails;
    private Boolean fraudAlerts;
    private Boolean noShowFollowUp;
    private Boolean quietHoursEnabled;
    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;
    private String preferredLanguage;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
