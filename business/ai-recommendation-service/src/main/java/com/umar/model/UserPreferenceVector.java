package com.umar.model;

import jakarta.persistence.*;
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
public class UserPreferenceVector {

    @Id
    private Long userId;

    private String preferenceVector;
    @ElementCollection
    private List<Long> topSalonIds;
    private String  bookingFrequencyTier;


}
