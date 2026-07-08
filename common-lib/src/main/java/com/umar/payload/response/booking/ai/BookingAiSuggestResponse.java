package com.umar.payload.response.booking.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingAiSuggestResponse {

    public List<Suggestion> suggestions;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Suggestion{
        private Integer rank;
        private String slotStartTime;
        private String slotEndTime;
        private String dayOfTheWeek;
        private String reason;
        private String matchScore;

    }
}
