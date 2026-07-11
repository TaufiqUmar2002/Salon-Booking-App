package com.umar.payload.response.aiAssistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PendingAction {

    public String actionType;
    private Long bookingId;
    private Long salonId;
    private Long serviceId;
    private String timeSlot;
    private String confirmationToken;
}
