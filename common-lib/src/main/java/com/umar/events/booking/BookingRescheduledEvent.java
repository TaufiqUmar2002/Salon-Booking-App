package com.umar.events.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingRescheduledEvent {
    private Long bookingId;
    private Long userId;
    private Long salonId;
    private LocalDateTime originalSlotStartTime;
    private LocalDateTime newSlotStartTime;
    private LocalDateTime newSlotEndTime;
    private Integer rescheduleCount;
    private LocalDateTime producedA;
}
