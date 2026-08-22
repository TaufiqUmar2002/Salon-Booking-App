package com.umar.service;

import com.umar.dedupe.IEventDeduplicationService;
import com.umar.events.user.UserProfileUpdatedEvent;
import com.umar.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    private final IEventDeduplicationService eventDeduplicationService;

    public void process(UserProfileUpdatedEvent event){
        if(!isRelevant(event)){
            log.info("Event is not relevant");
            return;
        }
        if(eventDeduplicationService.isAlreadyProcessed(event)){
            log.info("Duplicate event ignored. eventId={}, userId={}", event.getEventId(), event.getUserId());
        }
        if (hasField(event, "engagementData")) {
            processEngagement(event);
        }
        if (hasField(event, "notificationMetrics")) {
            processNotificationMetrics(event);
        }
        eventDeduplicationService.markProcessed(event);
        log.info("User profile update processed successfully. eventId={}, userId={}", event.getEventId(), event.getUserId());
    }

    private boolean isRelevant(UserProfileUpdatedEvent event){
        return event.getUpdatedFields() != null && !event.getUpdatedFields().isEmpty();
    }

    private boolean hasField(UserProfileUpdatedEvent event,String field){
        return event.getUpdatedFields()!=null && event.getUpdatedFields().contains(field);
    }


    private void processEngagement(UserProfileUpdatedEvent event){
        log.info("Processing engagement Data {}",event.getEngagementData());
        var engagementData = event.getEngagementData();
        if(engagementData != null){
            log.warn("Engagement Data is null");
        }
        // need to do further processing db and redis example
    }

    private void processNotificationMetrics(UserProfileUpdatedEvent event) {
        log.info("Processing notification metrics for user {}", event.getUserId());
        var notificationMetrics = event.getNotificationMetricsDto();
        if(notificationMetrics != null){
            log.warn("Notification Metrics is null");
        }
    }
}
