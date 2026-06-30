package salon_service.event;


import com.umar.events.category.CreateCategoryEventRequest;
import com.umar.events.category.UpdateCategoryEventRequest;
import com.umar.events.salon.SalonCreatedEvent;
import com.umar.events.salon.SalonDeletedEvent;
import com.umar.events.salon.SalonUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalonEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishSalonCreationEvent(SalonCreatedEvent event){
        log.info("[SalonEventProducer][publishSalonCreationEvent] {}" ,event);
        kafkaTemplate.send("salon.created", event);
    }

    public void publishSalonUpdatedEvent(SalonUpdatedEvent event){
        log.info("[SalonEventProducer][publishCategoryUpdateEvent] {}" ,event);
        kafkaTemplate.send("salon.updated",event);
    }

    public void publishSalonDeletedEvent(SalonDeletedEvent event){
        log.info("[SalonEventProducer][publishCategoryDeleteEvent] {}" ,event);
        kafkaTemplate.send("salon.deactivated",event);
    }

}
