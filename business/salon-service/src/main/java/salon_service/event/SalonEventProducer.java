package salon_service.event;


import com.umar.events.category.CreateCategoryEventRequest;
import com.umar.events.category.UpdateCategoryEventRequest;
import com.umar.events.salon.SalonCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalonEventProducer {

    public void publishSalonCreationEvent(SalonCreatedEvent event){
        log.info("[SalonEventProducer][publishSalonCreationEvent] {}" ,event);
    }

    public void publishCategoryUpdateEvent(UpdateCategoryEventRequest request){
        log.info("[SalonEventProducer][publishCategoryUpdateEvent] {}" ,request.getCategoryId());
    }

    public void publishCategoryDeleteEvent(UpdateCategoryEventRequest request){
        log.info("[SalonEventProducer][publishCategoryDeleteEvent] {}" ,request.getCategoryId());
    }

    public void publishCategoryRestoreEvent(CreateCategoryEventRequest request){
        log.info("[SalonEventProducer][publishCategoryRestoreEvent] {}" ,request.getCategoryId());
    }
}
