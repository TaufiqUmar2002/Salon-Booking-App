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
public class AiDescribeResponse {
    private Long serviceId;
    private String description;
    private Integer wordCount;
    private List<String> seoKeywords;
    private String tip;
}
