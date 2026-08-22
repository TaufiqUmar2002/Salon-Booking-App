package com.umar.dedupe;

import com.umar.events.user.UserProfileUpdatedEvent;

public interface IEventDeduplicationService {

    boolean isAlreadyProcessed(UserProfileUpdatedEvent event);
    void markProcessed(UserProfileUpdatedEvent event);
}
