package com.umar.repository;

import com.umar.model.ReviewAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewAuditRepository extends JpaRepository<ReviewAuditLog,Long> {
}
