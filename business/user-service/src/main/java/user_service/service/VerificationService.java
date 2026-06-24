package user_service.service;

import com.umar.exceptions.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import user_service.model.User;
import user_service.model.VerificationToken;
import user_service.repository.UserRepository;
import user_service.repository.VerificationTokenRepository;
import user_service.serviceInterface.IVerificationService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService implements IVerificationService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VerificationToken createToken(User user) {
        VerificationToken token = tokenRepository.findByUser(user).orElse(new VerificationToken());
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDate.now().atStartOfDay().plusHours(24));
        tokenRepository.save(token);
        return token;
    }

    @Override
    public void validateToken(String tokenValue) {
        VerificationToken verificationToken =this.getVerificationToken(tokenValue);
        this.isTokenExpired(verificationToken);
        if(verificationToken.getUser().getIsEmailVerified()){
            tokenRepository.delete(verificationToken);
            throw new ApiException(HttpStatus.CONFLICT,"ALREADY_VERIFIED","verifyEmail.error.emailAlreadyVerified");
        }
        User user =verificationToken.getUser();
        user.setIsEmailVerified(true);
        user.setIsActive(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        Optional<VerificationToken> verificationTokenOptional = tokenRepository.findByToken(token);
        if(verificationTokenOptional.isEmpty()){
            throw new ApiException(HttpStatus.NOT_FOUND,"TOKEN_NOT_FOUND","resetPassword.error.tokenNotFound");
        }
        return verificationTokenOptional.get();
    }

    @Override
    public void isTokenExpired(VerificationToken verificationToken) {
        if(verificationToken.isExpired()){
            tokenRepository.delete(verificationToken);
            throw new ApiException(HttpStatus.FORBIDDEN,"TOKEN_EXPIRED","verifyEmail.error.tokenExpired");
        }
    }

}
