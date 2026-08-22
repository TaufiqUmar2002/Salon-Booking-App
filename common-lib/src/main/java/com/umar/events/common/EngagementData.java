package com.umar.events.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EngagementData {
    private Integer appOpenTimes;

    private Integer emailOpenTimes;

    private Integer clickThroughTimes;

    private Integer avgResponseDelayMinutes;
}
