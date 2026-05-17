package com.umar.payload.request.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateCategoryRequest {
    private String name;
    private Long parentId;
    private String description;
    private String iconUrl;
    private Integer displayOrder;
    private Boolean isFeatured;
    private String metaTitle;
    private String metaDescription;
}
