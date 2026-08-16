package com.umar.events;

import com.umar.events.user.PasswordResetRequestedEvent;
import com.umar.events.user.UserInactiveEvent;
import com.umar.events.user.UserProfileUpdatedEvent;
import com.umar.events.user.UserRegisteredEvent;
import com.umar.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventConsumer {

    private final NotificationService notificationService;

//    @KafkaListener(
//            topics = "salon.user.profile.updated",
//            groupId = "notification-group",
//            containerFactory = "kafkaListenerContainerFactory"
//
//    )
//    public void consume(UserProfileUpdatedEvent event) {
//        log.info("User Updated Event Received {}", event);
////        notificationService.sendProfileUpdatedMail(event);
//    }

//    @KafkaListener(
//            topics = "salon.user.password.reset",
//            groupId = "notification-group-test",
//            containerFactory = "stringKafkaListenerContainerFactory"
//    )
//    public void consume(String message) {
//
//        System.out.println("=================================");
//        System.out.println("MESSAGE RECEIVED");
//        System.out.println(message);
//        System.out.println("=================================");
//    }

    @KafkaListener(
            topics = "salon.user.password.reset",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(PasswordResetRequestedEvent event){
        log.info("User Password reset Event Received {}", event);
    }

//    @KafkaListener(
//            topics = "salon.user.registered",
//            groupId = "notification-group",
//            containerFactory = "kafkaListenerContainerFactory"
//
//    )
//    public void  consume(UserRegisteredEvent event){
//        log.info("User Registered Event Received {}", event);
//
//    }

//    @KafkaListener(
//            topics = "salon.user.profile.inactive",
//            groupId = "notification-group",
//            containerFactory = "kafkaListenerContainerFactory"
//
//    )
//    public void consume(UserInactiveEvent event){
//        log.info("User Inactive Event Received {}", event);
//
//    }
}
