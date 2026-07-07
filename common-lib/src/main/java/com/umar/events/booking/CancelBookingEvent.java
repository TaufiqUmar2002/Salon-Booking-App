package com.umar.events.booking;

import com.umar.payload.enums.booking.CancelledBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CancelBookingEvent {
    private Long bookingId;
    private Long userId;
    private Long salonId;
    private Long serviceId;
    private LocalDateTime slotStartTime;
    private CancelledBy cancelledBy;
    private Boolean cancellationFeeApplied;
    private BigDecimal refundAmount;
    private LocalDateTime producedAt;
}
