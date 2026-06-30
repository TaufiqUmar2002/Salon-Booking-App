package com.umar.events.salon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SalonDeletedEvent {
    private Long salonId;
    private Long deactivatedBy;
    private LocalDateTime deactivatedAt;
    private String reason;

}
