package com.umar.payload.response.user.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SuggestionResponse {
    private Integer rank;
    private String categoryName;
    private String reason;
    private String estimatedPrice;
    private String bestTimeToBook;

}
