package com.umar.payload.response.salon.ai;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonGenerateDescriptionResponse {
    private Long salonId;
    private String generatedDescription;
    private Integer wordCount;
    private List<Faqs> faqs;
    private List<String> seoKeywords;
    private String tip;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Faqs{
        private String q;
        private String a;
    }

}
