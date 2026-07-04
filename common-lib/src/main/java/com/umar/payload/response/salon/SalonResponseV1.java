package com.umar.payload.response.salon;

import com.umar.payload.request.salon.ServiceSummary;
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
public class SalonResponseV1 {
    private Long salonId;
    private String name;
    private Long ownerId;
    private String description;
    private Long category;
    private String address;
    private String phone;
    private String email;
    private String website;
    private Map<String, String> openingHours;
    private List<ServiceSummary> services;
    private List<String> galleryUrls;
    private Double averageRating;
    private Integer totalReviews;
    private Boolean isVerified;


}
