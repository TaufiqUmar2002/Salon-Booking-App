package com.umar.payment_service.repository;

import com.umar.payment_service.model.PromoCodes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCodes, Long> {

    Optional<PromoCodes> findByPromoCode(String promoCode);
}
