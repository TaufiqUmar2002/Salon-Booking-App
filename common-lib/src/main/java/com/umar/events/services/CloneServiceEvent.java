package com.umar.events.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CloneServiceEvent {
    private Long newServiceId;
    private Long sourceServiceId;
    private Long salonId;
    private LocalDateTime producedAt;
}
