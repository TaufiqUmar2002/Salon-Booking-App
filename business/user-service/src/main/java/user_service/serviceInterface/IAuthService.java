package user_service.serviceInterface;

import com.umar.payload.request.user.AuthRequest;
import com.umar.payload.request.user.ForgotPasswordRequest;
import com.umar.payload.request.user.ResetPasswordRequest;
import com.umar.payload.response.user.AuthResponse;
import com.umar.payload.response.user.ForgotEmailResponse;
import com.umar.payload.response.user.LoginResponse;
import com.umar.payload.response.user.RefreshTokenResponse;
import user_service.dto.UserDTO;

public interface IAuthService {

    AuthResponse signUp(AuthRequest request) throws Exception;
    LoginResponse login(String email, String password,String ipAddress) throws Exception;
    RefreshTokenResponse refreshToken(String token);
    void verifyEmail(String verificationToken);
    ForgotEmailResponse forgotEmail(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);

}
