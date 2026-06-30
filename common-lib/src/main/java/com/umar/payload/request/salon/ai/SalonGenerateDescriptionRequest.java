package com.umar.payload.request.salon.ai;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonGenerateDescriptionRequest {
    private Long salonId;
    @Size(min = 1)
    private List<Integer> keywords;
    private String tone;
    private List<String> targetServices;
    private Integer maxLength;
    private Boolean includeFaqs;
}
