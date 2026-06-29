package com.umar.events;

import com.umar.events.category.CreateCategoryEventRequest;
import com.umar.events.category.UpdateCategoryEventRequest;
import com.umar.repository.NotificationRepository;
import com.umar.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        id = "search-indexing-consumer",
        topics = "salon.category.events",
        groupId = "search-indexing-group"
)
public class CategoryEventConsumer {

    private final NotificationService notificationService;

    @KafkaHandler
    public void handleCategoryCreated(CreateCategoryEventRequest request){
        log.info("[CategoryEventConsumer][handleCategoryCreated] {}",request);
    }

    @KafkaHandler
    public void handleCategoryUpdated(UpdateCategoryEventRequest request){
        log.info("[CategoryEventConsumer][handleCategoryUpdated] {}",request);
    }

    @KafkaHandler
    public void handleCategoryDeleted(UpdateCategoryEventRequest request){
        log.info("[CategoryEventConsumer][handleCategoryDeleted] {}",request);
    }

    @KafkaHandler
    public void handleCategoryRestore(UpdateCategoryEventRequest request){
        log.info("[CategoryEventConsumer][handleCategoryRestore] {}",request);
    }



}
