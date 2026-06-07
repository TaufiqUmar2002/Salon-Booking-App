package com.umar.payload.response.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ServiceResponseList {

    private List<ServiceResponse> serviceResponseList;
    private Integer totalServices;
    private Integer totalPages;
}
