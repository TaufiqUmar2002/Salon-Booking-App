package user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user_service.model.UserAuditLog;

public interface UserAuditLogRepository extends JpaRepository<UserAuditLog,Long> {
}
