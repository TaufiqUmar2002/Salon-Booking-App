package com.umar.payload.response.category.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Suggestions {

    private List<SuggestionV1> suggestionV1List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SuggestionV1 {
        private Integer rank;
        private Long categoryId;
        private String categoryName;
        private Double confidence;
        private String reason;
    }
}
