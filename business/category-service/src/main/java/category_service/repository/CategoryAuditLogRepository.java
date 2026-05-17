package category_service.repository;

import category_service.model.CategoryAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryAuditLogRepository extends JpaRepository<CategoryAuditLog,Long> {
}
