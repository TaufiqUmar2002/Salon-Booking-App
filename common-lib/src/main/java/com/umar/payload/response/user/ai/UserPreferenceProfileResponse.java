package com.umar.payload.response.user.ai;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferenceProfileResponse {

    private Long userId;

    private List<String> topCategories;

    private String averageSpendPerVisit;

    private String bookingFrequency;

    private String preferredDayOfWeek;

    private String preferredTimeOfDay;

    private Integer loyaltyScore;
    private  String aiSummary;

    private String lastComputedAt;
}
