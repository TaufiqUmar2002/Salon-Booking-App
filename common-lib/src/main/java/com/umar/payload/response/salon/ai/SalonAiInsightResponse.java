package com.umar.payload.response.salon.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonAiInsightResponse {
    private Long salonI;
    private String salonName;
    private Integer periodDays;
    private Integer totalBookings;
    private String cancellationRate;
    private String averageRevenuePerVisit;
    private String averageRevenuePerBooking;
    private String totalRevenue;
    private String totalCustomer;
    private List<ServiceInsightResponse> totalService;
    private String peakDayOfWeek;
    private String peakTimeOfDay;
    private String peakService;
    private String peakServiceRevenue;
    private String peakServiceBookingCount;
    private String customerSentiment;
    private SentimentBreakdown sentimentBreakdown;
    private String aiNarrative;
    private Recommendations recommendations;
    private String lastComputedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class ServiceInsightResponse{
        private String serviceName;
        private Integer bookingCount;
        private String revenue;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class  SentimentBreakdown{
        private String positive;
        private String negative;
        private String neutral;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Recommendations{
        private String rank;
        private String action;
        private String impact;
    }

}
