package com.umar.payload.request.services.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiBulkDescribeRequest {
    private String tone;
    private Integer maxWords;
    private Boolean runDry;
    private List<String> serviceIds;
}
