package com.umar.payload.request.salon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateSalonRequest {

    private String name;
    private String description;
    private String category;
    private String phone;
    private String email;
    private String website;
    private Map<String, String> openingHours;
    private List<ServiceSummary> services;
    private Boolean isVerified;

}
