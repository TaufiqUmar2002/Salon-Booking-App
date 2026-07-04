package com.umar.payload.response.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchServiceResponseList {

    private List<SearchServiceResponse> responseList = new ArrayList<>();

    private Integer totalResult;
    private Integer totalPages;
    private Long salonId;
    private String salonName;
    private Long categoryId;
    private BigDecimal MinPrice;
    private BigDecimal maxPrice;
    private BigDecimal averagePrice;
    private Integer durationMinutes;
    private Double averageRating;
    private Double relevanceScore;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class SearchServiceResponse{

        private Long serviceId;
        private String serviceName;

    }

}
