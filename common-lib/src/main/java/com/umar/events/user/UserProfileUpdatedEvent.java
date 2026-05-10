package com.umar.events.user;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileUpdatedEvent {

    private Long userId;
    private List<String> updatedFields;
    private LocalDateTime updatedAt;

}
