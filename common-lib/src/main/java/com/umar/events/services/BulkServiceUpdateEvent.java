package com.umar.events.services;

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
public class BulkServiceUpdateEvent {
    private List<Long> affectedServiceIds;
    private List<String> changedFields;
    private LocalDateTime updatedAt;
}
