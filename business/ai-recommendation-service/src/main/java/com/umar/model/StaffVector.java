package com.umar.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class StaffVector {

    private Long staffId;
    private String  staffVector;
    private Long salonId;
    @ElementCollection
    private List<String> specialties;
    private Integer satisfactionScore;
    private Integer totalReviewsTagged;

}
