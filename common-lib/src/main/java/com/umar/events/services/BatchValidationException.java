package com.umar.events.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class BatchValidationException extends RuntimeException {
    private Map<String,String> errors;

    public BatchValidationException(Map<String, String> errors) {
        super("Batch validation failed for " + errors.size() + " items");
        this.errors = errors;
    }
}
