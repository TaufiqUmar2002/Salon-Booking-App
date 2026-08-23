package com.umar.repository;

import com.umar.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("""
  SELECT count(r) FROM Review r where r.bookingId =:bookingId
""")
    Integer getReviewByBookingId(@Param("bookingId") Long bookingId);

    List<Review> findBySalonId(Long salonId);

    @Query("""
    SELECT r FROM Review r where r.salonId =:salonId and r.isVisible = false and r.isDeleted = false
    and r.sentimentLabel is not null and r.isSpam = false LIMIT :maxReviews
    """)
    Optional<List<Review>> findBySalonIdAndIsVisibleFalse(Long reviewId,Integer maxReviews);
}
