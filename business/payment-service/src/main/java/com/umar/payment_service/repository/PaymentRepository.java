package com.umar.payment_service.repository;

import com.umar.payload.enums.payment.PaymentStatus;
import com.umar.payment_service.model.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentRecord,Long> {

    @Query("""
SELECT 1 FROM PaymentRecord WHERE bookingId = :bookingId AND STATUS IN (:status)
""")
    Long paymentExistsByBookingId(@Param("bookingId") Long bookingId,@Param("status") List<PaymentStatus> status);

    Optional<PaymentRecord> findByPaymentRef(String paymentRef);
}
