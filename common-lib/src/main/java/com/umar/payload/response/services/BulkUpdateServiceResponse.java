package com.umar.payload.response.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkUpdateServiceResponse {

    private Long updatedCount;

    private List<Long> updatedIds;

    private String message;
}
