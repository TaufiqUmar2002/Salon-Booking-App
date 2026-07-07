package com.umar.payload.response.services.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiServicePerformance {
    private Long salonId;
    private Integer periodDays;
    private List<TopPerformer> topPerformers;
    private List<UnderPerformer> underPerformers;
    private String aiNarrative;
    private List<RevenueByCategory> revenueByCategories;
    private String  lastComputedAT;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class TopPerformer{
        private Long serviceId;
        private String serviceName;
        private String revenue;
        private String fillRate;
        private Double avgRating;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class UnderPerformer{
        private Long serviceId;
        private String issue;
        private String recommendation;

    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class RevenueByCategory{
        private Long categoryId;
        private String categoryName;
        private Long totalRevenue;
        private Long bookingCount;
    }

}
