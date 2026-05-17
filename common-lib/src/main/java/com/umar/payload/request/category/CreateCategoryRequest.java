package com.umar.payload.request.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateCategoryRequest {

    @Size(min = 1,max = 80)
    private String name;

    private Long parentId;

    @Size(max = 1000)
    private String description;

    private String iconUrl;

    private Integer displayOrder;

    private Boolean isFeatured;

    @Size(max = 60)
    private String metaTitle;

    @Size(max = 160)
    private String metaDescription;

}
