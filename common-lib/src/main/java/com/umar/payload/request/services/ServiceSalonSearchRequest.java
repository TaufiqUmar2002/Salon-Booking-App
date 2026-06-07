package com.umar.payload.request.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ServiceSalonSearchRequest {
    private Long categoryId;
    private Boolean includeInactive;
    private Boolean featured;
    private String sortBy;
    private String sortDir;
    private Integer page;
    private Integer size;
}
