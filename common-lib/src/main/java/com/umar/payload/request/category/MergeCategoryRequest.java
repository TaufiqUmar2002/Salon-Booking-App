package com.umar.payload.request.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MergeCategoryRequest {
    private Long targetCategoryId;
    private String reason;
}
