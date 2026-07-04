package com.umar.service_offering.event;

import com.umar.events.services.BulkServiceUpdateEvent;
import com.umar.events.services.CloneServiceEvent;
import com.umar.events.services.ServiceCreatedEvent;
import com.umar.events.services.UpdateServiceEvent;
import com.umar.payload.request.services.DeleteServiceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceOfferingEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishServiceCreatedEvent(ServiceCreatedEvent event){
        log.info("[ServiceOfferingEventProducer][publishServiceCreatedEvent]");
        kafkaTemplate.send("service.created",event);
    }

    public void publishServiceUpdateEvent(UpdateServiceEvent event){
        log.info("[ServiceOfferingEventProducer][publishServiceUpdateEvent]");
        kafkaTemplate.send("service.updated",event);
    }
    public void publishServiceDeleteEvent(DeleteServiceEvent event){
        log.info("[ServiceOfferingEventProducer][publishServiceDeleteEvent]");
        kafkaTemplate.send("service.deleted",event);
    }

    public void publishBulkServiceUpdateEvent(BulkServiceUpdateEvent event){
        log.info("[ServiceOfferingEventProducer][publishBulkServiceUpdateEvent] {}",event);
        kafkaTemplate.send("service.bulk.update",event);

    }

    public void publishCloneServiceEvent(CloneServiceEvent serviceEvent){
        log.info("[ServiceOfferingEventProducer][publishCloneServiceEvent] {}",serviceEvent);
        kafkaTemplate.send("service.clone",serviceEvent);


    }

}
