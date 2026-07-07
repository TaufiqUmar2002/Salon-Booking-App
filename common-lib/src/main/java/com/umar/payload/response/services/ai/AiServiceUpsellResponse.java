package com.umar.payload.response.services.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiServiceUpsellResponse {
    private Long primaryServiceId;
    private List<Suggestions> suggestions;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Suggestions{
        private Long rank;
        private Long serviceId;
        private String serviceName;
        private BigDecimal price;
        private Integer durationMinutes;
        private String coBookingRate;
        private String reason;

    }


}
