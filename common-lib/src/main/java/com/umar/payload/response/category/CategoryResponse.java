package com.umar.payload.response.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryResponse {

    private Long categoryId;
    private String name;
    private String slug;
    private Long parentId;
    private Integer level;
    private String iconUrl;
    private Integer displayOrder;
    private Boolean isFeatured;

    private Integer salonCount;
    private Integer bookingCount;

    // Used only for tree format
    private List<CategoryResponse> children;

}
