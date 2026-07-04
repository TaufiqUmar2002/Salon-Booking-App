package com.umar.payload.response.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkUpdateServiceResponse {

    private Long updatedCount;

    private List<Long> updatedIds;

    private List<Long> filedIds;

    private Long failedCount;

    private Map<String,String> message;
}
