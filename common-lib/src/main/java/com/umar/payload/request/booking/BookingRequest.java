package com.umar.payload.request.booking;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingRequest {

    @NotNull
    private Long salonId;
    @NotNull
    private Long serviceId;
    private Long staffId;
    @NotNull
    private String slotStartTime;
    private String  customerNotes;
}
