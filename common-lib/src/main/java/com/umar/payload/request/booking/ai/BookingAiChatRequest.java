package com.umar.payload.request.booking.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingAiChatRequest implements Serializable {
    private String message;
    private String sessionId;
}
