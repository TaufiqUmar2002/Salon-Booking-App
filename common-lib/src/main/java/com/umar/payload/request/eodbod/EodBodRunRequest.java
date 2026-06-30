package com.umar.payload.request.eodbod;

import com.umar.payload.enums.eodbod.CycleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EodBodRunRequest {

    private CycleType cycleType;
    private Long salonId;
    private String businessDate;
    private List<String> processKeys;
    private Boolean force;

}
