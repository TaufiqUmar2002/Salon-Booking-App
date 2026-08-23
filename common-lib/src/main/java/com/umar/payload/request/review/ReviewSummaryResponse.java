package com.umar.payload.request.review;

import com.umar.payload.enums.review.SentimentLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewSummaryResponse {

    private Long salonId;
    private String summaryText;
    private Integer reviewCount;
    private Double avgRating;
    private Map<String,Double> sentimentDistribution;
    private List<String>  topPositiveThemes;
    private List<String>  topNegativeThemes;
    private Boolean fromCache;
    private String cacheExpireAt;
    private LocalDateTime expireAt;
}
