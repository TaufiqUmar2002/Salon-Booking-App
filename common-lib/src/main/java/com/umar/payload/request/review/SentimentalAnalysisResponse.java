package com.umar.payload.request.review;

import com.umar.payload.enums.review.SentimentLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SentimentalAnalysisResponse {
    private SentimentLabel sentiment;
    private Double score;
    private List<String> themes;
    private String summaryOneLine;

}
