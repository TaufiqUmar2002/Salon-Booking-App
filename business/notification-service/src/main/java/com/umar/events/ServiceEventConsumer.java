package com.umar.events;

import com.umar.events.services.BulkServiceUpdateEvent;
import com.umar.events.services.CloneServiceEvent;
import com.umar.events.services.ServiceCreatedEvent;
import com.umar.events.services.UpdateServiceEvent;
import com.umar.payload.request.services.DeleteServiceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceEventConsumer {

    @KafkaListener(
            topics = "salon.service.created",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleServiceCreatedEvent(ServiceCreatedEvent serviceCreatedEvent){
        log.info("Service created event {}",serviceCreatedEvent);
    }

    @KafkaListener(
            topics = "salon.service.updated",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleServiceUpdateEvent(UpdateServiceEvent updateServiceEvent){
        log.info("Service update event {}",updateServiceEvent);
    }

    @KafkaListener(
            topics = "salon.service.deleted",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleServiceDeleteEvent(DeleteServiceEvent deleteServiceEvent){
        log.info("Service delete event {}",deleteServiceEvent);
    }

    @KafkaListener(
            topics = "salon.service.bul.update",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleBulkServiceUpdateEvent(BulkServiceUpdateEvent bulkServiceUpdateEvent){
        log.info("Bulk service update event {}",bulkServiceUpdateEvent);
    }

    @KafkaListener(
            topics = "salon.service.clone",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleCloneServiceEvent(CloneServiceEvent cloneServiceEvent){
        log.info("Clone service event {}",cloneServiceEvent);
    }


}
