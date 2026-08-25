package com.umar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "TENANT")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private LocalDateTime businessDate;

    private LocalDateTime eodProcessTillDate;

    private LocalDateTime bodProcessTillDate;

    private LocalDateTime lastEodRun;

    private LocalDateTime lastBodRun;

    private String status;

    private String lastRunBy;


}
