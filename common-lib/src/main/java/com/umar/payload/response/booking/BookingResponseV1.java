package com.umar.payload.response.booking;

import com.umar.payload.enums.booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingResponseV1 {
    private Long bookingId;
    private Long userId;
    private Long salonId;
    private Long serviceId;
    private Long staffId;
    private LocalDateTime slotStartTime;
    private LocalDateTime slotEndTime;
    private BookingStatus status;
    private String totalPrice;
    private String currency;
    private String customerNotes;
    private String cancellationReason;
    private String cancelledBy;
    private String rescheduledFrom;
    private String createdAt;
    private String updatedAt;
}
