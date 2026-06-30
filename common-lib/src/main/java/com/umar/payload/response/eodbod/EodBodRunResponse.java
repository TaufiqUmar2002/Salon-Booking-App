package com.umar.payload.response.eodbod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EodBodRunResponse {
    private Long runId;
    private String runKey;
    private String cycleType;
    private String status;
    private Integer totalSteps;
    private String progressChannel;
    private String createdAt;
}
