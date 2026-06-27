package com.umar.payload.request.category;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @Pattern(
            regexp = "^https://[a-zA-Z0-9.\\-_]+.s3([a-zA-Z0-9.\\-_]+)?\\.amazonaws\\.com/.*$",
            message = "iconUrl must be a valid S3 URL"
    )
    private String iconUrl;

    @Positive(message = "displayOrder must be a positive integer")
    private Integer displayOrder;

    private Boolean isFeatured;

    @Size(max = 60, message = "Meta title must be less than 60 characters")
    private String metaTitle;

    @Size(max = 160,message = "Meta Description must be less than 160 characters")
    private String metaDescription;

}
