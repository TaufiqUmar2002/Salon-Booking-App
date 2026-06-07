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
import java.util.Set;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering,Long> {

    Set<ServiceOffering> findBySalonId(Long salonId);

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
}
