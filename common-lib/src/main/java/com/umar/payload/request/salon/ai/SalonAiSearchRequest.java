package com.umar.payload.request.salon.ai;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SalonAiSearchRequest {

    @NotNull
    @Size(min = 3, max = 100)
    private String q;
    private Double lat;
    private Double lng;
    private Integer page;
    private Integer size;
    private Integer radiusKm;
}
