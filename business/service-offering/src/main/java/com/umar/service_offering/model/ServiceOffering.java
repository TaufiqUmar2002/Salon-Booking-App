package com.umar.service_offering.model;


import com.umar.payload.constants.GenderType;
import com.umar.payload.constants.ServiceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ServiceOffering  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salon_id", nullable = false)
    private Long  salonId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 200)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "buffer_before_minutes")
    private Integer bufferBeforeMinutes;

    @Column(name = "buffer_after_minutes")
    private Integer bufferAfterMinutes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "discounted_price", precision = 10, scale = 2)
    private BigDecimal discountedPrice;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "required_deposit")
    private Boolean requiredDeposit;

    @ElementCollection
    @CollectionTable(
            name = "staff_ids",
            joinColumns = @JoinColumn(name = "staff")
    )
    @Column(name = "tag")
    private List<Long> staffIds;

    @Column(name = "DEPOSIT_AMOUNT")
    private BigDecimal depositAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type")
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "max_bookings_per_day")
    private Integer maxBookingsPerDay;

    @Column(name = "cancellation_allowed", nullable = false)
    private Boolean cancellationAllowed = true;

    @Column(name = "cancellation_window_hours")
    private Integer cancellationWindowHours;

    @Column(name = "advance_booking_days")
    private Integer advanceBookingDays;

    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Stored as JSON or comma-separated values
     */
    @ElementCollection
    @CollectionTable(
            name = "service_tags",
            joinColumns = @JoinColumn(name = "service_id")
    )
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(name = "rating_average")
    private Double ratingAverage;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;



    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Version
    private Integer version;

    private Long clonedFromId;

    private Integer bookingCount;


    private String image;

}
