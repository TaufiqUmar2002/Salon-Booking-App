package com.umar.events.salon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonCreatedEvent {
    private Long salonId;
    private Long ownerId;
    private String reason;
    private String name;
    private String slug;
    private Long categoryId;
    private Boolean isVerified;
    private LocalDateTime createdAt;
}
