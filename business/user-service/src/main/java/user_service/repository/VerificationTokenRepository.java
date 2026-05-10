package user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user_service.model.User;
import user_service.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long>{

    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
}
