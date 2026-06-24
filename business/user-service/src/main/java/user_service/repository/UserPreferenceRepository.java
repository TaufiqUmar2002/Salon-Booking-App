package user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user_service.model.UserPreference;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference,Long> {
    Optional<List<UserPreference>> getUserPreferenceByUserId(Long userId);
}
