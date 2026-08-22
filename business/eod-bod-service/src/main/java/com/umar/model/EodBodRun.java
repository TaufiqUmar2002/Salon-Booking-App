package com.umar.model;

import com.umar.payload.enums.eodbod.CycleType;
import com.umar.payload.enums.eodbod.Status;
import com.umar.payload.enums.eodbod.TriggerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EOD_BOD_RUN_MST")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class EodBodRun {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String runKey;
    @Enumerated(EnumType.STRING)
    private CycleType cycleType;
    private Long salonId;
    private LocalDate businessDate;
    @Enumerated(EnumType.STRING)
    private TriggerType triggerType;
    private String triggeredBy;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer totalSteps;
    private Integer completedSteps;
    private Integer failedSteps;
    private String lockKey;
    private LocalDateTime startedAt;
    private LocalDate completedAt;
    private Long durationMins;
    @CreatedDate
    private LocalDateTime createdAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @LastModifiedBy
    private String updatedBy;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "eodBodRun")
    private EodBodRunStep eodBodRunStep;


}
