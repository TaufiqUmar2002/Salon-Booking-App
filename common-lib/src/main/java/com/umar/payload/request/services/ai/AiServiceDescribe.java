package com.umar.payload.request.services.ai;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AiServiceDescribe {
    @NotNull
    private Long serviceId;
    private List<String> keywords;
    private String tone;
    private Integer maxWords;
    private Boolean includeTime;
}
