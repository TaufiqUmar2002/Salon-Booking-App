package category_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class CategoryAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long categoryId;
    private Long adminId;
    private String action;

    @ElementCollection
    private Map<String,String> changeFields;
    private String reason;
    @CreatedDate
    private LocalDateTime performedAt;
}
