//package user_service.service;
//
//import com.umar.exceptions.common.exception.ApiException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import user_service.model.User;
//import user_service.model.VerificationToken;
//import user_service.repository.UserRepository;
//import user_service.repository.VerificationTokenRepository;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class VerificationServiceTest {
//
//    @Mock
//    private VerificationTokenRepository tokenRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private VerificationService verificationService;
//
//    private User testUser;
//    private VerificationToken testToken;
//
//    @BeforeEach
//    void setUp() {
//        testUser = User.builder()
//                .id(1L)
//                .email("test@example.com")
//                .firstName("John")
//                .lastName("Doe")
//                .isEmailVerified(false)
//                .isActive(false)
//                .build();
//
//        testToken = VerificationToken.builder()
//                .id(1L)
//                .token("test-token-123")
//                .user(testUser)
//                .expiryDate(LocalDate.now().atStartOfDay().plusHours(24))
//                .build();
//    }
//
//    @Test
//    void createToken_WhenNoExistingToken_ShouldCreateNewToken() {
//        when(tokenRepository.findByUser(testUser)).thenReturn(Optional.empty());
//        when(tokenRepository.save(any(VerificationToken.class))).thenReturn(testToken);
//
//        VerificationToken result = verificationService.createToken(testUser);
//
//        assertNotNull(result);
//        assertEquals(testUser, result.getUser());
//        assertNotNull(result.getToken());
//        assertNotNull(result.getExpiryDate());
//        verify(tokenRepository).save(any(VerificationToken.class));
//    }
//
//    @Test
//    void createToken_WhenExistingToken_ShouldUpdateExistingToken() {
//        when(tokenRepository.findByUser(testUser)).thenReturn(Optional.of(testToken));
//        when(tokenRepository.save(any(VerificationToken.class))).thenReturn(testToken);
//
//        VerificationToken result = verificationService.createToken(testUser);
//
//        assertNotNull(result);
//        assertEquals(testUser, result.getUser());
//        verify(tokenRepository).save(any(VerificationToken.class));
//    }
//
//    @Test
//    void validateToken_WhenValidToken_ShouldVerifyUser() {
//        when(tokenRepository.findByToken("test-token-123")).thenReturn(Optional.of(testToken));
//        when(userRepository.save(any(User.class))).thenReturn(testUser);
//
//        verificationService.validateToken("test-token-123");
//
//        assertTrue(testUser.getIsEmailVerified());
//        assertTrue(testUser.getIsActive());
//        verify(userRepository).save(any(User.class));
//        verify(tokenRepository).delete(testToken);
//    }
//
//    @Test
//    void validateToken_WhenTokenNotFound_ShouldThrowException() {
//        when(tokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            verificationService.validateToken("invalid-token");
//        });
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//        assertEquals("TOKEN_NOT_FOUND", exception.getErrorCode());
//    }
//
//    @Test
//    void validateToken_WhenTokenExpired_ShouldThrowException() {
//        testToken.setExpiryDate(LocalDateTime.now().minusHours(1));
//        when(tokenRepository.findByToken("test-token-123")).thenReturn(Optional.of(testToken));
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            verificationService.validateToken("test-token-123");
//        });
//
//        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
//        assertEquals("TOKEN_EXPIRED", exception.getErrorCode());
//        verify(tokenRepository).delete(testToken);
//    }
//
//    @Test
//    void validateToken_WhenEmailAlreadyVerified_ShouldThrowException() {
//        testUser.setIsEmailVerified(true);
//        when(tokenRepository.findByToken("test-token-123")).thenReturn(Optional.of(testToken));
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            verificationService.validateToken("test-token-123");
//        });
//
//        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
//        assertEquals("ALREADY_VERIFIED", exception.getErrorCode());
//        verify(tokenRepository).delete(testToken);
//    }
//
//    @Test
//    void getVerificationToken_WhenTokenExists_ShouldReturnToken() {
//        when(tokenRepository.findByToken("test-token-123")).thenReturn(Optional.of(testToken));
//
//        VerificationToken result = verificationService.getVerificationToken("test-token-123");
//
//        assertNotNull(result);
//        assertEquals(testToken, result);
//    }
//
//    @Test
//    void getVerificationToken_WhenTokenNotFound_ShouldThrowException() {
//        when(tokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            verificationService.getVerificationToken("invalid-token");
//        });
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//        assertEquals("TOKEN_NOT_FOUND", exception.getErrorCode());
//    }
//
//    @Test
//    void isTokenExpired_WhenTokenNotExpired_ShouldNotThrowException() {
//        testToken.setExpiryDate(LocalDateTime.now().plusHours(1));
//
//        assertDoesNotThrow(() -> verificationService.isTokenExpired(testToken));
//    }
//
//    @Test
//    void isTokenExpired_WhenTokenExpired_ShouldThrowExceptionAndDeleteToken() {
//        testToken.setExpiryDate(LocalDateTime.now().minusHours(1));
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            verificationService.isTokenExpired(testToken);
//        });
//
//        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
//        assertEquals("TOKEN_EXPIRED", exception.getErrorCode());
//        verify(tokenRepository).delete(testToken);
//    }
//}
