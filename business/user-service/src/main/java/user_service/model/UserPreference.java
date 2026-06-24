package user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table
@Entity
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long userId;
    private String categoryName;
    private String reason;
    private String estimatedPrice;
    private String bestTimeToBook;
    @Lob
    private String request;
}
