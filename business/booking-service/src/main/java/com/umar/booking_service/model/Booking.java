package com.umar.booking_service.model;

import com.umar.payload.enums.booking.BookingStatus;
import com.umar.payload.enums.booking.CancelledBy;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
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
    private Long customerId;
    private String customerNotes;
    private Long kafkaOffset;
    private Boolean cancellationFeeApplied;
    private BigDecimal cancellationFee;
    private Integer rescheduleCount;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Long totalServices;

    @LastModifiedBy
    private String updatedBy;

    private LocalDateTime completedAt;


}
