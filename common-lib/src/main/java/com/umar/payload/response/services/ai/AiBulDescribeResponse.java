package com.umar.payload.response.services.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiBulDescribeResponse {
    private Integer processedCount;
    private Integer skippedCount;
    private Boolean dryRun;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Describe{
        private Long serviceId;
        private String name;
        private String description;
        private Boolean saved;
    }
}
