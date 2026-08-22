package com.umar.model;

import com.umar.payload.enums.eodbod.CycleType;
import com.umar.payload.enums.eodbod.Scope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "EOD_BOD_CONFIG_MST")
@EntityListeners(AuditingEntityListener.class)
public class EodBodConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String processKey;
    @Enumerated(EnumType.STRING)
    private CycleType cycleType;
    private String displayName;
    private String description;
    private Boolean enabled;
    private Integer sequenceOrder;
    private Boolean critical;
    private Long durationMins;
    private Integer timeoutSeconds;
    private Integer maxAttempts;
    private Long retryBackOffMs;
    @Enumerated(EnumType.STRING)
    private Scope scope;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
