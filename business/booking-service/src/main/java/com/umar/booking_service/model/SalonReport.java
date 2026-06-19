package com.umar.booking_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalonReport {
    private Long salonId;
    private String salonName;
    private double totalEarnings;
    private Integer totalBookings;
    private Integer cancelledBookings;
    private Double totalRefunds;

}
