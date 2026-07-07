package com.umar.payload.response.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingSummaryResponse {
    private Long salonId;
    private String	 periodFrom;
    private String periodTo;
    private Integer totalBookings;
    private Integer confirmedBookings;
    private Integer completedBookings;
    private Integer cancelledBookings;
    private Integer noShowBookings;
    private Integer noShowRate;
    private String cancellationRate;
    private String totalRevenue;
    private String occupancyRate;
    private String busiestDay;
    private String topService;
}
