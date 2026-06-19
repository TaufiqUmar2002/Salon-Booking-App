package com.umar.payload.response.booking;

import com.umar.payload.constants.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserBookingResponse {

    private List<BookingSummary> summary;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer size;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingSummary{
        private Long bookingId;
        private Long salonId;
        private Long serviceId;
        private String  slotStartTime;
        private BookingStatus status;
        private String totalPrice;
    }
}
