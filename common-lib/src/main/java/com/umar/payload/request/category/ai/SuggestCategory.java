package com.umar.payload.request.category.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestCategory {
    private String serviceName;
    private String serviceDescription;
    private Integer topN;
}
