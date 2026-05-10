package com.umar.payload.request;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalonRequest {

    private String name;

    private List<String> images;

    private String address;
    private String phoneNumber;
    private String email;

    private String city;

    private String ownerId;

    private LocalTime openTime;

    private LocalTime closeTime;
}
