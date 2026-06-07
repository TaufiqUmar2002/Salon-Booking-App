package com.umar.payload.response.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchServiceResponseList {

    private List<SearchServiceResponse> responseList;

    private Long totalResult;
    private Integer totalPages;
    private String name;
    private Long salonId;
    private String salonName;
    private Long categoryId;
    private String price;
    private Integer durationMinutes;
    private Double averageRating;
    private Double relevanceScore;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    static class SearchServiceResponse{

        private Long serviceId;

    }

}
