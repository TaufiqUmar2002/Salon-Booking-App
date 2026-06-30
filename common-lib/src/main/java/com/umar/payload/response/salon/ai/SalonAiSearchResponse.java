package com.umar.payload.response.salon.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonAiSearchResponse {
    private String query;
    private ExtractFilters extractFilters;
    List<ResponseList> result;
    private Long totalResult;
    private Integer totalPages;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class ExtractFilters{
        private Long categoryId;
        private String city;
        private String dayOfWeek;
        private BigDecimal priceRange;
        private String serviceKeywords;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class ResponseList{
        private Long salonId;
        private String name;
        private String category;
        private String city;
        private Double averageRating;
        private Double matchScore;
        private String matchReason;
        private Double distanceKm;
    }

}
