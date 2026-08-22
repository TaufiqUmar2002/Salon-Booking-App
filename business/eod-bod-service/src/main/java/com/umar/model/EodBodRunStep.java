package com.umar.model;

import com.umar.payload.enums.eodbod.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Stack;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "EOD_BOD_RUN_STEP_MST")
@EntityListeners(AuditingEntityListener.class)
public class EodBodRunStep {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", referencedColumnName = "id")
    private EodBodRun eodBodRun;
    private String processKey;
    private Integer sequenceOrder;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Boolean critical;
    private Integer attemptCount;
    private Integer maxAttempts;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Long durationMins;
    private String resultSummary;
    private String errorMessage;
}
