package user_service.serviceInterface;

import user_service.model.User;
import user_service.model.VerificationToken;

public interface IVerificationService {

    VerificationToken createToken(User user);
    void validateToken(String tokenValue);
    VerificationToken getVerificationToken(String token);
    void isTokenExpired(VerificationToken token);
}
