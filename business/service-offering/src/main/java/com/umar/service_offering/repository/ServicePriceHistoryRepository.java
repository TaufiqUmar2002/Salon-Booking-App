package com.umar.service_offering.repository;

import com.umar.service_offering.model.ServicePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePriceHistoryRepository extends JpaRepository<ServicePriceHistory ,Long> {
}
