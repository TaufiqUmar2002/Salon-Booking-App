package com.umar.tools;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public  class AiAssistantToolMO {
        private String name;
        private String description;
        private String type;
        private String parameters;
    
    }
