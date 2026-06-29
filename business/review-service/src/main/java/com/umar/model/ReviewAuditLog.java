package com.umar.model;

import com.umar.payload.enums.review.DeleteReview;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Table(name = "REVIEW_AUDIT_LOG")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReviewAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long adminId;
    private Long reviewId;
    private String action;
    private DeleteReview reason;
    private String reasonNote;
    private LocalDateTime performedAt;

}
