package com.umar.exceptions.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeignErrorResponse {

    private Integer status;
    private String code;
    private String message;
    private Map<String, Object> response;
    private String timestamp;

}
