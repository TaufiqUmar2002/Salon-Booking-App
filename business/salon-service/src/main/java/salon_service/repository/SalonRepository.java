package salon_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import salon_service.model.Salon;

import java.util.Optional;


@Repository
public interface SalonRepository extends JpaRepository<Salon,Long>{

    Salon findByOwnerId(Long ownerId);

    @Query("""
      select s from Salon s where s.name=:name and s.ownerId=:ownerId
    """)
    Optional<Salon> findSalonByNameAndOwnerId(@Param("name") String name, @Param("ownerId") Long ownerId);


    @Query(
            value = """
        SELECT *
        FROM salon s
        WHERE s.is_verified = true
          AND s.is_active = true

          AND (
                :category IS NULL
                OR s.category_id = :category
          )

          AND (
                :lat IS NULL
                OR :lng IS NULL
                OR :radius IS NULL

                OR (
                    6371 * acos(
                        cos(radians(:lat))
                        * cos(radians(s.latitude))
                        * cos(radians(s.longitude) - radians(:lng))
                        + sin(radians(:lat))
                        * sin(radians(s.latitude))
                    )
                ) <= :radius
          )
        ORDER BY s.created_at DESC
    """,

            countQuery = """
        SELECT COUNT(*)
        FROM salon s
        WHERE s.is_verified = true
          AND s.is_active = true

          AND (
                :category IS NULL
                OR s.category_id = :category
          )

          AND (
                :lat IS NULL
                OR :lng IS NULL
                OR :radius IS NULL

                OR (
                    6371 * acos(
                        cos(radians(:lat))
                        * cos(radians(s.latitude))
                        * cos(radians(s.longitude) - radians(:lng))
                        + sin(radians(:lat))
                        * sin(radians(s.latitude))
                    )
                ) <= :radius
          )
    """,

            nativeQuery = true
    )
    Page<Salon> findBrowseSalons(
            @Param("category") Long category,
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("radius") Double radius,
            Pageable pageable);

}
