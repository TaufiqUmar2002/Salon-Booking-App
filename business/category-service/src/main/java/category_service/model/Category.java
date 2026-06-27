package category_service.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    private String  slug;

    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    private Integer level;

    private String iconUrl;

    private Integer displayOrder;

    private Boolean isActive;

    private Boolean isFeatured;

    @ManyToOne
    @JoinColumn(name = "merged_id")
    private Category mergedIntoId;

    private Integer salonCount;

    private Integer bookingCount;

    private String  metaTitle;

    private String metaDescription;

    @CreatedDate
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;


    @PrePersist
    @PreUpdate
    public void setSlug() {
        if (name == null || name.isBlank()) {
            slug = null;
            return;
        }
        this.slug = name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }


}
