package com.umar.payload.request.category.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GenerateDescription {
    private Long categoryId;
    private String tone;
    private List<String> keywords;
    private Integer maxWords;
    private Boolean includeFlags;
}
