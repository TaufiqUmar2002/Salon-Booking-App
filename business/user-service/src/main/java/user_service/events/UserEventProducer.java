package user_service.events;

import com.umar.events.user.PasswordResetRequestedEvent;
import com.umar.events.user.UserInactiveEvent;
import com.umar.events.user.UserProfileUpdatedEvent;
import com.umar.events.user.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserProfileUpdatedEvent> userProfileUpdatedEventKafkaTemplate;
    private final KafkaTemplate<String ,UserRegisteredEvent> userRegisteredEventKafkaTemplate;
    private final KafkaTemplate<String, UserInactiveEvent> userInactiveEventKafkaTemplate;
    private final KafkaTemplate<String, PasswordResetRequestedEvent> passwordResetRequestedEventKafkaTemplate;


    public void publishPasswordResetEvent(PasswordResetRequestedEvent event) {
        log.info("user password reset event publish {} ",event);
        passwordResetRequestedEventKafkaTemplate.send("salon.user.password.reset", event.getUserId(), event);
    }
    public void publishUserRegisteredEvent(UserRegisteredEvent event){
        log.info("user registered event publish {} ",event);
        userRegisteredEventKafkaTemplate.send("salon.user.registered",event);
    }

    public void publishUserProfileUpdateEvent(UserProfileUpdatedEvent event){
        log.info("user profile updated {}",event);
        userProfileUpdatedEventKafkaTemplate.send("salon.user.profile.updated",event);
    }

    public void publishUserInactiveEvent(UserInactiveEvent event){
        log.info("publishUserInactiveEvent {}",event);
        userInactiveEventKafkaTemplate.send("salon.user.profile.updated",event);
    }

}
