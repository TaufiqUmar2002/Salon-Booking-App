package com.umar.payment_service.repository;

import com.umar.payment_service.model.FraudSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudRepository extends JpaRepository<FraudSignal,Long> {
}
