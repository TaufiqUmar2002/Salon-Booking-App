package com.umar.events.booking;

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
public class BookingCompletedEvent {
    private Long bookingId;
    private Long userId;
    private Long salonId;
    private Long serviceId;
    private Long staffId;
    private LocalDateTime slotStartTime;
    private BigDecimal totalPrice;
    private LocalDateTime completedAt;
    private LocalDateTime producedAt;
}
