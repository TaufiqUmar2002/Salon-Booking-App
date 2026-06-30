package com.umar.events.salon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonUpdatedEvent {
    private Long salonId;
    private Long ownerId;
    private Long UpdatedBy;
    private List<String> updatedFields;
    private LocalDateTime updatedAt;

}
