package com.umar.exceptions.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private int status;
    private String code;
    private String message;
    private Object response;
    private LocalDateTime timestamp;
}
