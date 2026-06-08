package user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user_service.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("""
       SELECT u
       FROM User u
       WHERE u.lastBookingDate <= :cutoffDate
       """)
    List<User> findInactiveUsers(
            @Param("cutoffDate")
            LocalDateTime cutoffDate);
}
