package com.umar.events.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateServiceEvent {
    private  Long serviceId;
    private Long salonId;
    private List<String> updatedFields;
    private LocalDateTime updatedAt;
}
