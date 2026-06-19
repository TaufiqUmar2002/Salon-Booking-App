package category_service.event;


import com.umar.events.category.CreateCategoryEventRequest;
import com.umar.events.category.UpdateCategoryEventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryEventProducer {

    public void publishCategoryCreationEvent(CreateCategoryEventRequest request){
        log.info("[CategoryEventProducer][publishCategoryCreationEvent] {}" ,request.getCategoryId());
    }

    public void publishCategoryUpdateEvent(UpdateCategoryEventRequest request){
        log.info("[CategoryEventProducer][publishCategoryUpdateEvent] {}" ,request.getCategoryId());
    }

    public void publishCategoryDeleteEvent(UpdateCategoryEventRequest request){
        log.info("[CategoryEventProducer][publishCategoryDeleteEvent] {}" ,request.getCategoryId());
    }

    public void publishCategoryRestoreEvent(CreateCategoryEventRequest request){
        log.info("[CategoryEventProducer][publishCategoryRestoreEvent] {}" ,request.getCategoryId());
    }
}
