package user_service.events;

import com.umar.events.user.PasswordResetRequestedEvent;
import com.umar.events.user.UserProfileUpdatedEvent;
import com.umar.events.user.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventProducer {

//    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishPasswordResetEvent(PasswordResetRequestedEvent event) {
        log.info("user password reset event publish {} ",event);
//        kafkaTemplate.send(TOPIC, event.getUserId(), event);
    }
    public void publishUserRegisteredEvent(UserRegisteredEvent event){
        log.info("user registered event publish {} ",event);

    }

    public void publishUserProfileUpdateEvent(UserProfileUpdatedEvent event){
        log.info("user profile updated {}",event);
    }

    //    @KafkaListener(topics = "salon.user.registered", groupId = "recommendation-group")
//    public void seedPreferenceProfile(UserRegisteredEvent event) {
//        System.out.println("Seeding preference profile for user: " + event.getUserId());
//        // profileRepo.save(new UserPreference(event.getUserId()));
//    }

}
