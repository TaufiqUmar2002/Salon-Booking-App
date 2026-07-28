package user_service.controller;


import com.umar.payload.request.user.AuthRequest;
import com.umar.payload.request.user.ForgotPasswordRequest;
import com.umar.payload.request.user.LoginRequest;
import com.umar.payload.request.user.ResetPasswordRequest;
import com.umar.payload.response.user.AuthResponse;
import com.umar.payload.response.user.ForgotEmailResponse;
import com.umar.payload.response.user.LoginResponse;
import com.umar.payload.response.user.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid AuthRequest request){
        AuthResponse authResponse = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) throws Exception {
        String ipAddress = this.getClientIp(request);
        LoginResponse loginResponse = authService.login(loginRequest.getEmail(),loginRequest.getPassword(),ipAddress);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestParam("refresh-token") String refreshToken){
        RefreshTokenResponse response =authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam("verification-token") String verificationToken){
        authService.verifyEmail(verificationToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotEmailResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest passwordRequest){
        ForgotEmailResponse response = authService.forgotEmail(passwordRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        authService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }



    private String getClientIp(HttpServletRequest request){
        String xForwardFor = request.getHeader("X-Forward-For");
        if(xForwardFor==null || xForwardFor.isBlank()){
            return request.getRemoteAddr();
        }
        return xForwardFor.split(",")[0].trim();
    }

}
