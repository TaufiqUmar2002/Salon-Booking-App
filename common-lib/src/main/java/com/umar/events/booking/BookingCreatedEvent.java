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
public class BookingCreatedEvent {
    private Long bookingId;
    private Long userId;
    private Long salonId;
    private Long serviceId;
    private Long staffId;
    private LocalDateTime slotStartTime;
    private LocalDateTime slotEndTime;
    private BigDecimal totalPrice;
    private String customerEmail;
    private String customerPhone;
    private LocalDateTime producedAt;
}
