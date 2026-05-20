package com.umar.payload.response.salon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonResponseData {

    private Long salonId;
    private String name;
    private String category;
    private String city;
    private Double averageRating;
    private Integer totalReviews;
    private Double distanceKm;
    private String thumbNail;

}
