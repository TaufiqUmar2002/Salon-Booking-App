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
public class BookingAiChatResponse {
    private String message;
    private String sessionId;
    private Long bookingId;
    private String actionTaken;
    private List<String> suggestions;
}
