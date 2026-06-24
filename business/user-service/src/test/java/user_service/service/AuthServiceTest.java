//package user_service.service;
//
//import com.umar.events.user.PasswordResetRequestedEvent;
//import com.umar.events.user.UserRegisteredEvent;
//import com.umar.exceptions.common.exception.ApiException;
//import com.umar.payload.request.user.AuthRequest;
//import com.umar.payload.request.user.ForgotPasswordRequest;
//import com.umar.payload.request.user.ResetPasswordRequest;
//import com.umar.payload.response.user.AuthResponse;
//import com.umar.payload.response.user.ForgotEmailResponse;
//import com.umar.payload.response.user.LoginResponse;
//import com.umar.payload.response.user.RefreshTokenResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import user_service.constants.UserRole;
//import user_service.events.UserEventProducer;
//import user_service.mapper.UserMapper;
//import user_service.model.User;
//import user_service.model.VerificationToken;
//import user_service.repository.UserRepository;
//import user_service.serviceInterface.IVerificationService;
//import user_service.util.JwtUtil;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AuthServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtUtil jwtProvider;
//
//    @Mock
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Mock
//    private IVerificationService verificationService;
//
//    @Mock
//    private UserEventProducer eventProducer;
//
//    @Mock
//    private UserMapper userMapper;
//
//    @Mock
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Mock
//    private ValueOperations<String, String> valueOperations;
//
//    @InjectMocks
//    private AuthService authService;
//
//    private User testUser;
//    private VerificationToken testToken;
//    private AuthRequest authRequest;
//
//    @BeforeEach
//    void setUp() {
//        testUser = User.builder()
//                .id(1L)
//                .email("test@example.com")
//                .firstName("John")
//                .lastName("Doe")
//                .passwordHash("encodedPassword")
//                .role(UserRole.CUSTOMER)
//                .isEmailVerified(true)
//                .isActive(true)
//                .refreshToken(null)
//                .build();
//
//        testToken = VerificationToken.builder()
//                .id(1L)
//                .token("test-token-123")
//                .user(testUser)
//                .expiryDate(LocalDateTime.now().plusHours(24))
//                .build();
//
//        authRequest = AuthRequest.builder()
//                .email("test@example.com")
//                .password("password123")
//                .role(UserRole.CUSTOMER)
//                .firstName("John")
//                .lastName("Doe")
//                .build();
//
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//    }
//
//    @Test
//    void signUp_WhenNewUser_ShouldCreateUserAndPublishEvent() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        when(userMapper.toEntity(any(AuthRequest.class))).thenReturn(testUser);
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(userRepository.save(any(User.class))).thenReturn(testUser);
//        when(verificationService.createToken(any(User.class))).thenReturn(testToken);
//        doNothing().when(eventProducer).publishUserRegisteredEvent(any(UserRegisteredEvent.class));
//
//        AuthResponse response = authService.signUp(authRequest);
//
//        assertNotNull(response);
//        assertEquals(testUser.getId(), response.getUserId());
//        assertEquals(testUser.getEmail(), response.getEmail());
//        verify(userRepository).save(any(User.class));
//        verify(eventProducer).publishUserRegisteredEvent(any(UserRegisteredEvent.class));
//    }
//
//    @Test
//    void signUp_WhenEmailExists_ShouldThrowException() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.signUp(authRequest);
//        });
//
//        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
//        assertEquals("EMAIL_CONFLICT", exception.getErrorCode());
//    }
//
//    @Test
//    void signUp_WhenRoleIsAdmin_ShouldThrowException() {
//        authRequest.setRole(UserRole.ADMIN);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.signUp(authRequest);
//        });
//
//        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
//        assertEquals("INVALID_ROLE", exception.getErrorCode());
//    }
//
//    @Test
//    void login_WithValidCredentials_ShouldReturnLoginResponse() {
//        when(valueOperations.get(anyString())).thenReturn(null);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//        when(jwtProvider.generateRefreshToken(any(User.class))).thenReturn("refreshToken");
//        when(jwtProvider.generateAccessToken(any(User.class))).thenReturn("accessToken");
//        when(jwtProvider.getAccessTokenExpiry(anyString())).thenReturn("expiryDate");
//        when(userRepository.save(any(User.class))).thenReturn(testUser);
//
//        LoginResponse response = authService.login("test@example.com", "password123", "127.0.0.1");
//
//        assertNotNull(response);
//        assertEquals("accessToken", response.getAccessToken());
//        assertEquals("refreshToken", response.getRefreshToken());
//        assertEquals("Bearer", response.getTokenType());
//        verify(redisTemplate).delete(anyString());
//    }
//
//    @Test
//    void login_WithInvalidCredentials_ShouldThrowException() {
//        when(valueOperations.get(anyString())).thenReturn(null);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
//        when(valueOperations.increment(anyString())).thenReturn(1L);
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.login("test@example.com", "wrongpassword", "127.0.0.1");
//        });
//
//        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
//        assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
//    }
//
//    @Test
//    void login_WhenEmailNotVerified_ShouldThrowException() {
//        testUser.setIsEmailVerified(false);
//        when(valueOperations.get(anyString())).thenReturn(null);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//        when(valueOperations.increment(anyString())).thenReturn(1L);
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.login("test@example.com", "password123", "127.0.0.1");
//        });
//
//        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
//        assertEquals("EMAIL_NOT_VERIFIED", exception.getErrorCode());
//    }
//
//    @Test
//    void login_WhenAccountDeactivated_ShouldThrowException() {
//        testUser.setIsActive(false);
//        when(valueOperations.get(anyString())).thenReturn(null);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//        when(valueOperations.increment(anyString())).thenReturn(1L);
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.login("test@example.com", "password123", "127.0.0.1");
//        });
//
//        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
//        assertEquals("ACCOUNT_DEACTIVATED", exception.getErrorCode());
//    }
//
//    @Test
//    void login_WhenRateLimitExceeded_ShouldThrowException() {
//        when(valueOperations.get(anyString())).thenReturn("5");
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.login("test@example.com", "password123", "127.0.0.1");
//        });
//
//        assertEquals(HttpStatus.TOO_MANY_REQUESTS, exception.getStatus());
//        assertEquals("RATE_LIMIT_EXCEEDED", exception.getErrorCode());
//    }
//
//    @Test
//    void refreshToken_WithValidToken_ShouldReturnNewTokens() {
//        testUser.setRefreshToken("oldRefreshToken");
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
//        when(jwtProvider.isTokenExpired(anyString())).thenReturn(false);
//        when(jwtProvider.generateAccessToken(any(User.class))).thenReturn("newAccessToken");
//        when(jwtProvider.generateRefreshToken(any(User.class))).thenReturn("newRefreshToken");
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewRefreshToken");
//        when(jwtProvider.getAccessTokenExpiry(anyString())).thenReturn("expiryDate");
//        when(userRepository.save(any(User.class))).thenReturn(testUser);
//
//        RefreshTokenResponse response = authService.refreshToken("oldRefreshToken");
//
//        assertNotNull(response);
//        assertEquals("newAccessToken", response.getAccessToken());
//        assertEquals("newRefreshToken", response.getRefreshToken());
//    }
//
//    @Test
//    void refreshToken_WithExpiredToken_ShouldThrowException() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
//        when(jwtProvider.isTokenExpired(anyString())).thenReturn(true);
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.refreshToken("expiredToken");
//        });
//
//        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
//        assertEquals("INVALID_REFRESH_TOKEN", exception.getErrorCode());
//    }
//
//    @Test
//    void refreshToken_WithInvalidUser_ShouldThrowException() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.refreshToken("token");
//        });
//
//        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
//        assertEquals("ACCOUNT_DEACTIVATED", exception.getErrorCode());
//    }
//
//    @Test
//    void verifyEmail_WithValidToken_ShouldVerifyEmail() {
//        doNothing().when(verificationService).validateToken(anyString());
//
//        assertDoesNotThrow(() -> authService.verifyEmail("validToken"));
//        verify(verificationService).validateToken("validToken");
//    }
//
//    @Test
//    void forgotEmail_WithValidEmail_ShouldSendResetToken() {
//        ForgotPasswordRequest request = new ForgotPasswordRequest("test@example.com");
//        when(valueOperations.get(anyString())).thenReturn(null);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
//        when(verificationService.createToken(any(User.class))).thenReturn(testToken);
//        when(userRepository.save(any(User.class))).thenReturn(testUser);
//        doNothing().when(eventProducer).publishPasswordResetEvent(any(PasswordResetRequestedEvent.class));
//
//        ForgotEmailResponse response = authService.forgotEmail(request);
//
//        assertNotNull(response);
//        verify(eventProducer).publishPasswordResetEvent(any(PasswordResetRequestedEvent.class));
//    }
//
//    @Test
//    void forgotEmail_WithInvalidEmail_ShouldThrowException() {
//        ForgotPasswordRequest request = new ForgotPasswordRequest("invalid@example.com");
//        when(valueOperations.get(anyString())).thenReturn(null);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        when(valueOperations.increment(anyString())).thenReturn(1L);
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.forgotEmail(request);
//        });
//
//        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
//        assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
//    }
//
//    @Test
//    void resetPassword_WithValidToken_ShouldResetPassword() {
//        ResetPasswordRequest request = new ResetPasswordRequest("test-token-123", "newPassword123");
//        when(verificationService.getVerificationToken(anyString())).thenReturn(testToken);
//        doNothing().when(verificationService).isTokenExpired(any(VerificationToken.class));
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
//        when(userRepository.save(any(User.class))).thenReturn(testUser);
//
//        assertDoesNotThrow(() -> authService.resetPassword(request));
//        verify(userRepository).save(any(User.class));
//    }
//
//    @Test
//    void resetPassword_WithSamePassword_ShouldThrowException() {
//        ResetPasswordRequest request = new ResetPasswordRequest("test-token-123", "oldPassword");
//        when(verificationService.getVerificationToken(anyString())).thenReturn(testToken);
//        doNothing().when(verificationService).isTokenExpired(any(VerificationToken.class));
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            authService.resetPassword(request);
//        });
//
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//        assertEquals("SAME_PASSWORD", exception.getErrorCode());
//    }
//}
