package user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class UserAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long adminId;

    private Long targetUserId;

    private String action;

    private String reason;

    @CreatedDate
    private LocalDateTime timeStamp;

}
