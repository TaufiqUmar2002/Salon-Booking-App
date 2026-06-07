package com.umar.service_offering.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ServiceDTO {


    private Long id;
    private String name;

    private String description;

    private int price;

    private  int duration;

    private Long salonId;

    private Long categoryId;

    private String image;
}
