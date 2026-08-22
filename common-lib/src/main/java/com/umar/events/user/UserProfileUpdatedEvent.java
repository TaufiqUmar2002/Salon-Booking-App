package com.umar.events.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umar.events.common.EngagementData;
import com.umar.events.common.NotificationMetricsDto;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileUpdatedEvent {

    private Long eventId;
    private Long userId;
    private List<String> updatedFields;
    private EngagementData engagementData;
    private NotificationMetricsDto notificationMetricsDto;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;

}
