package com.umar.payload.response.user.ai;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse {

    private List<SuggestionResponse> suggestions;
}
