package category_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
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
    private LocalDateTime performedAt;
}
