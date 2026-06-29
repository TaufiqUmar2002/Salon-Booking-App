package com.umar.payload.request.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeleteServiceEvent {
    private Long serviceId;
    private Long salonId;
    private LocalDateTime deletedAt;
}
