package category_service.events;


import com.umar.events.category.CategoryMergeEvent;
import com.umar.events.category.CreateCategoryEventRequest;
import com.umar.events.category.UpdateCategoryEventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishCategoryCreationEvent(CreateCategoryEventRequest eventRequest){
        log.info("[CategoryEventProducer][publishCategoryCreationEvent] {}" ,eventRequest);
        kafkaTemplate.send("salon.category.events",eventRequest.getCategoryId().toString(),eventRequest);
    }

    public void publishCategoryUpdateEvent(UpdateCategoryEventRequest eventRequest){
        log.info("[CategoryEventProducer][publishCategoryUpdateEvent] {}" ,eventRequest);
        kafkaTemplate.send("salon.category.events",eventRequest.getCategoryId().toString(),eventRequest);
    }

    public void publishCategoryDeleteEvent(UpdateCategoryEventRequest request){
        log.info("[CategoryEventProducer][publishCategoryDeleteEvent] {}" ,request.getCategoryId());
        kafkaTemplate.send("salon.category.events",request.getCategoryId().toString(),request);
    }

    public void publishCategoryRestoreEvent(CreateCategoryEventRequest request){
        log.info("[CategoryEventProducer][publishCategoryRestoreEvent] {}" ,request.getCategoryId());
        kafkaTemplate.send("salon.category.events",request.getCategoryId().toString(),request);
    }

    public void publishCategoryMergeEvent(CategoryMergeEvent mergeEvent){
        log.info("[CategoryEventProducer][publishCategoryMergeEvent] {}",mergeEvent.getSourceCategoryId());
        kafkaTemplate.send("salon.category.events",mergeEvent);
    }
}
