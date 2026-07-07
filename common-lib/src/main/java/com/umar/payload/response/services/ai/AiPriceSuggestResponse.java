package com.umar.payload.response.services.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiPriceSuggestResponse {

    private Long serviceId;
    private BigDecimal suggestedPrice;
    private BigDecimal currentPrice;
    private String priceDelta;
    private String confidenceScore;
    private String demandScore;
    private String cancellationRate;
    private String competitorAvgPrice;
    private String rationale;
    private String riskNote;
    private String analysisWindowDays;

}
