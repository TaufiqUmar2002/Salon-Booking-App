package salon_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salon_service.model.SalonAuditLog;

public interface SalonAuditRepository extends JpaRepository<SalonAuditLog,Long> {
}
