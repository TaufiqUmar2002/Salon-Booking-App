package com.umar.service_offering.repository;

import com.umar.service_offering.model.ServiceOffering;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering,Long> {


    @Query("""
    select so from ServiceOffering so where so.salonId=:salonId and (so.description is not null or so.description != '')
    """)
    Optional<List<ServiceOffering>> findBySalonIdAndDescriptionNotNull(Long salonId);

    @Query("""
    select so from ServiceOffering so where so.salonId=:salonId and so.categoryId=:categoryId
    """)
    @QueryHints(value =
            {
                    @QueryHint(name = HibernateHints.HINT_READ_ONLY, value = "true"),
                    @QueryHint(name = HibernateHints.HINT_READ_ONLY, value = "true")
            })
    Page<ServiceOffering> searchServiceBySalonId(@Param("salonId") Long salonId, @Param("categoryId") Long categoryId, Pageable pageable);

    ServiceOffering findServiceOfferingByName(String name);

    @Query("""
    select so from ServiceOffering so where so.salonId=:salonId and so.categoryId=:categoryId and so.name like %:query% and so.price between :minPrice and :maxPrice and so.durationMinutes <= :maxDuration
    """)
    List<ServiceOffering> searchServices(String query, Long salonId, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Integer maxDuration, Pageable pageable);

}
