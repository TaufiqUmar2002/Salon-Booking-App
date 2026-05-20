package com.umar.payload.response.salon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalonResponseList {

    private List<SalonResponseData> salonResponseDataList;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;

}
