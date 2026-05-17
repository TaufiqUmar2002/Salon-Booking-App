package com.umar.events.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateCategoryEventRequest {
    private Long categoryId;

    private String name;

    private String description;

    private LocalDateTime createdAt;
}
