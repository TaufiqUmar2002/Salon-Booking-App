package user_service.scheduler;

import com.umar.events.user.UserInactiveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import user_service.events.UserEventProducer;
import user_service.model.User;
import user_service.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InactiveUserScheduler {

    private final UserRepository repository;
    private final UserEventProducer eventProducer;


    @Scheduled(cron = "0 0 2 */7 * *")
    public void publishInactiveUsers() {
        List<User> inactiveUsers = repository.findInactiveUsers(LocalDateTime.now());
        inactiveUsers.forEach(user -> {
            UserInactiveEvent event =
                    UserInactiveEvent.builder()
                            .userId(user.getId())
                            .email(user.getEmail())
                            .lastActiveAt(
                                    user.getLastBookingDate())
                            .inactiveDays(
                                    calculateInactiveDays(user))
                            .build();
            eventProducer.publishUserInactiveEvent(event);
        });
    }

    private Integer calculateInactiveDays(User user) {
        return (int) ChronoUnit.DAYS.between(
                user.getLastBookingDate(),
                LocalDateTime.now());
    }
}
