package com.umar.payload.request.salon.ai;

import com.umar.payload.enums.salon.PriceRange;
import com.umar.payload.enums.salon.SortBy;
import com.umar.payload.enums.salon.TimeOfDay;
import com.umar.payload.enums.services.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SalonSearchFilters {

    private GenderType category;
    private String area;
    private DayOfWeek dayOfWeek;
    private TimeOfDay timeOfDay;
    private PriceRange priceRange;
    private List<String> serviceKeywords;
    private List<String> qualitySignals;
    private List<String> amenities;
    private SortBy sortBy;
}
