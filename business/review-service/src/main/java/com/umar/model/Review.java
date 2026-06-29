package com.umar.model;

import com.umar.payload.enums.review.SentimentLabel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long bookingId;
    private Long salonId;
    private Long userId;
    private Integer rating;
    private String title;
    private String body;
    private String serviceName;
    private String stylistName;
    @ElementCollection
    private List<String> mediaUrls;
    @Enumerated(EnumType.STRING)
    private SentimentLabel sentimentLabel;
    private Double sentimentScore;
    private LocalDateTime sentimentScoredAt;
    private Boolean isSpam;
    private Double spamScore;
    private String spamFlagReason;
    private Boolean isVisible;
    private Boolean isAdminDeleted;
    private String  ownerReply;
    private LocalDateTime ownerReplyAt;
    private String aiDraft;
    private Integer helpfulCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
