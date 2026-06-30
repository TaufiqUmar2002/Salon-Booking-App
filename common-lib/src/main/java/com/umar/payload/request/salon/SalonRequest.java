package com.umar.payload.request.salon;




import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalonRequest {

    private String name;
    private Long categoryId;
    private String addressLine1;
    private String city;
    private String state;
    private String  postalCode;
    private Double latitude;
    private Double longitude;
    private String phone;
    private Map<String, String> openingHours = new HashMap<>();
    private List<ServiceSummary> services = new ArrayList<>();
    private String description;
    @Email
    private String email;
    private String reason;
    private String website;
    private List<String> galleryUrls;
}
