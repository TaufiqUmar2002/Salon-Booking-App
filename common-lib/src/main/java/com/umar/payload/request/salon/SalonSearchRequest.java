package com.umar.payload.request.salon;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonSearchRequest {
    @Min(0)
    private int page = 0; // Default page

    @Min(1)
    @Max(100)
    private int size = 10; // Default size limit

    private Long category;
    private Double latitude;
    private Double longitude;
    private Double radiusInKm;
}
