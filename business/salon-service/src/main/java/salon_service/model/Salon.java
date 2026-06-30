package salon_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SALON")
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String name;

    private String slug;

    private String description;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String phone;

    private String email;

    private String website;

    @Convert(converter = OpeningHoursConverter.class)
    @Column(name = "opening_hours", columnDefinition = "TEXT")
    private Map<String, String> openingHours = new HashMap<>();

    @ElementCollection
    @CollectionTable(
            name = "salon_services",
            joinColumns = @JoinColumn(name = "salon_id")
    )
    private List<ServiceSummary> services = new ArrayList<>();

    /**
     * Salon gallery images
     */
    @ElementCollection
    @CollectionTable(
            name = "salon_gallery",
            joinColumns = @JoinColumn(name = "salon_id")
    )
    @Column(name = "image_url")
    private List<String> galleryUrls = new ArrayList<>();

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "total_reviews", nullable = false)
    private Integer totalReviews = 0;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
