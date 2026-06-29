package com.umar.payload.request.user.ai;

import com.umar.payload.enums.user.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OnwardSuggestionRequest {

    private HairType hairType;
    private SkinType skinType;
    private Interest interests;
    private BudgestRange budgetRange;
    private Frequency frequency;
}
