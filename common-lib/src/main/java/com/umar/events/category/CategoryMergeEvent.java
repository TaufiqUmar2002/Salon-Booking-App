package com.umar.events.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryMergeEvent {
    private Long sourceCategoryId;
    private Long targetCategoryId;
}
