package com.umar.booking_service.model;

import com.umar.payload.constants.BookingStatus;
import com.umar.payload.constants.CancelledBy;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long salonId;
    private Long userId;
    private Long serviceId;
    private Long staffId;
    private LocalDateTime slotStartTime;
    private LocalDateTime slotEndTime;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private String cancellationReason;
    private CancelledBy cancelledBy;
    private LocalDateTime rescheduledFrom;
    private LocalDateTime noShowTime;
    private BigDecimal totalPrice;
    private String currency;
    private String customerNotes;
    private Long kafkaOffset;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;


}
