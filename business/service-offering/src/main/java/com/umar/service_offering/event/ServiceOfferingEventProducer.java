package com.umar.service_offering.event;

import com.umar.events.services.BulkServiceUpdateEvent;
import com.umar.events.services.CloneServiceEvent;
import com.umar.events.services.ServiceCreatedEvent;
import com.umar.events.services.UpdateServiceEvent;
import com.umar.payload.request.services.DeleteServiceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceOfferingEventProducer {

    public void publishServiceCreatedEvent(ServiceCreatedEvent event){
        log.info("[ServiceOfferingEventProducer][publishServiceCreatedEvent]");
    }

    public void publishServiceUpdateEvent(UpdateServiceEvent event){
        log.info("[ServiceOfferingEventProducer][publishServiceUpdateEvent]");
    }
    public void publishServiceDeleteEvent(DeleteServiceEvent event){
        log.info("[ServiceOfferingEventProducer][publishServiceDeleteEvent]");
    }

    public void publishBulkServiceUpdateEvent(BulkServiceUpdateEvent event){
        log.info("[ServiceOfferingEventProducer][publishBulkServiceUpdateEvent] {}",event);
    }

    public void publishCloneServiceEvent(CloneServiceEvent serviceEvent){
        log.info("[ServiceOfferingEventProducer][publishCloneServiceEvent] {}",serviceEvent);

    }

}
