package com.umar.payload.response.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LookupCategory {
    private Long categoryId;
    private String name;
    private String slug;
    private Boolean isActive;
    private Long mergeIntoId;
}
