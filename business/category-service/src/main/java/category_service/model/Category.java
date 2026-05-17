package category_service.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long createdBy;


    @PrePersist
    @PreUpdate
    public void setSlug() {
        this.slug = name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }


}
