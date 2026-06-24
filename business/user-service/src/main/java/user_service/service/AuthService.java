package user_service.service;

import com.umar.events.user   .PasswordResetRequestedEvent;
import com.umar.events.user.UserRegisteredEvent;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.user.AuthRequest;
import com.umar.payload.request.user.ForgotPasswordRequest;
import com.umar.payload.request.user.ResetPasswordRequest;
import com.umar.payload.response.user.AuthResponse;
import com.umar.payload.response.user.ForgotEmailResponse;
import com.umar.payload.response.user.LoginResponse;
import com.umar.payload.response.user.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user_service.constants.UserRole;
import user_service.events.UserEventProducer;
import user_service.mapper.UserMapper;
import user_service.model.User;
import user_service.model.VerificationToken;
import user_service.repository.UserRepository;
import user_service.serviceInterface.IAuthService;
import user_service.serviceInterface.IVerificationService;
import user_service.util.JwtUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final IVerificationService verificationService;
    private final UserEventProducer eventProducer;
    private static final Long BLOCK_MINUTES = 15L;
    private final UserMapper userMapper;
    private static final Long MAX_ATTEMPTS = 5L;
    private final RedisTemplate<String, String> redisTemplate;
    private final Executor executor; ;

    @Override
    public AuthResponse signUp(AuthRequest request) {
        Optional<User> existingUser= userRepository.findByEmail(request.getEmail().trim().toLowerCase());
        if(existingUser.isPresent()){
            throw new ApiException(HttpStatus.CONFLICT, "EMAIL_CONFLICT", "registration.error.emailExists");
        }
        if(request.getRole().name().equals(UserRole.ADMIN.name())){
           throw new ApiException(HttpStatus.UNPROCESSABLE_CONTENT,"INVALID_ROLE","registration.error.role");
        }
        User newUser = userMapper.toEntity(request);
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        newUser.setIsActive(Boolean.FALSE);
        User savedUser = userRepository.save(newUser);
        VerificationToken verificationToken = verificationService.createToken(savedUser);
        log.info("verification token is : {}",verificationToken.getToken());
        UserRegisteredEvent registeredEvent = UserRegisteredEvent.builder()
                .email(savedUser.getEmail())
                .verificationToken(verificationToken.getToken())
                .firstName(savedUser.getFirstName())
                .producedAt(LocalDateTime.now())
                .role(savedUser.getRole().name())
                .userId(savedUser.getId())
                .build();
        eventProducer.publishUserRegisteredEvent(registeredEvent);
        return AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .firstname(savedUser.getFirstName())
                .message("registration.success.message").build();
    }



    @Override
    public LoginResponse login(String email, String password,String ipAddress) throws Exception {
        validateRateLimit(ipAddress);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty() || !passwordEncoder.matches(password,userOptional.get().getPasswordHash())){
            recordFailedAttempt(ipAddress);
            throw new ApiException(HttpStatus.UNAUTHORIZED,"INVALID_CREDENTIALS","login.error.invalidEmailOrPassword");
        }
        User user = userOptional.get();
        if(!user.getIsEmailVerified()){
            recordFailedAttempt(ipAddress);
            throw new ApiException(HttpStatus.FORBIDDEN,"EMAIL_NOT_VERIFIED","login.error.emailNotVerified");
        }
        if(!user.getIsActive()){
            recordFailedAttempt(ipAddress);
            throw new ApiException(HttpStatus.FORBIDDEN,"ACCOUNT_DEACTIVATED","login.error.accountDeactivated");
        }
        clearFailedAttempts(ipAddress);
        String jwtRefreshToken = user.getRefreshToken();
        if(jwtRefreshToken==null || jwtProvider.isTokenExpired(jwtRefreshToken)){
            jwtRefreshToken=jwtProvider.generateRefreshToken(user);
        }
        String jwtAccessToken = jwtProvider.generateAccessToken(user);
        user.setRefreshToken(jwtRefreshToken);
        user.setUpdatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return LoginResponse
                .builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .accessTokenExpiry(jwtProvider.getAccessTokenExpiry(jwtAccessToken))
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(String token) {
        String email = jwtProvider.extractUserEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty() || !userOptional.get().getIsActive()){
            throw new ApiException(HttpStatus.FORBIDDEN,"ACCOUNT_DEACTIVATED","login.error.accountDeactivated");
        }
        User user = userOptional.get();
        if(jwtProvider.isTokenExpired(token) || !token.equals(user.getRefreshToken())){
            throw new ApiException(HttpStatus.UNAUTHORIZED,"INVALID_REFRESH_TOKEN","refreshToken.error.InvalidRefreshToken");
        }
        String jwtAccessToken  = jwtProvider.generateAccessToken(user);
        String jwtRefreshToken = jwtProvider.generateRefreshToken(user);
        user.setRefreshToken(passwordEncoder.encode(jwtRefreshToken));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return RefreshTokenResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .accessTokenExpiry(jwtProvider.getAccessTokenExpiry(jwtAccessToken))
                .build();
    }

    @Override
    public void verifyEmail(String verificationToken){
            verificationService.validateToken(verificationToken);
    }

    @Override
    public ForgotEmailResponse forgotEmail(ForgotPasswordRequest request) {
        validateRateLimit(request.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(optionalUser.isEmpty()){
            recordFailedAttempt(request.getEmail());
            throw new ApiException(HttpStatus.UNAUTHORIZED,"INVALID_CREDENTIALS","forgotEmail.error.invalidCredentials");
        }
        User user =optionalUser.get();
        VerificationToken passwordResetToken = verificationService.createToken(user);
        user.setPasswordResetToken(passwordResetToken.getToken());
        userRepository.save(user);
        clearFailedAttempts(request.getEmail());
        executor.execute(
                ()->{
                    PasswordResetRequestedEvent resetRequestedEvent =PasswordResetRequestedEvent
                            .builder()
                            .email(user.getEmail())
                            .resetToken(passwordResetToken.getToken())
                            .userId(user.getId().toString())
                            .build();
                    eventProducer.publishPasswordResetEvent(resetRequestedEvent);
                }
        );

        return ForgotEmailResponse.builder().message("forgotEmail.success.message").build();
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        VerificationToken token = verificationService.getVerificationToken(request.getTempToken());
        verificationService.isTokenExpired(token);
        String hashedPassword = passwordEncoder.encode(request.getNewPassword());
        if(passwordEncoder.matches(request.getNewPassword(),token.getUser().getPasswordHash())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"SAME_PASSWORD","resetPassword.error.samePassword");
        }
        User user = token.getUser();
        user.setPasswordHash(hashedPassword);
        user.setUpdatedAt(LocalDateTime.now());
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    private Authentication authentication(String email,String password) throws Exception {
        UserDetails userDetails =customUserDetailsService.loadUserByUsername(email);
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new Exception("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


    private void validateRateLimit(String ipAddress){
        String key = "login:fail:" + ipAddress;
        String attempts = redisTemplate.opsForValue().get(key);
        if(attempts!=null &&Long.parseLong(attempts)>=MAX_ATTEMPTS){
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS,"RATE_LIMIT_EXCEEDED", "login.error.toManyAttempts");
        }
    }
    private void recordFailedAttempt(String ipAddress) {
        String key = "login:fail:" + ipAddress;
        String attempts = redisTemplate.opsForValue().get(key);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(BLOCK_MINUTES));
        }
    }

    private void clearFailedAttempts(String ipAddress) {
        redisTemplate.delete("login:fail:" + ipAddress);
    }
}
