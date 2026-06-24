//package user_service.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
//import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
//import org.springframework.test.context.ActiveProfiles;
//import user_service.constants.UserRole;
//import user_service.model.User;
//import user_service.model.VerificationToken;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@ActiveProfiles("test")
//class VerificationTokenRepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private VerificationTokenRepository tokenRepository;
//
//    private User testUser;
//    private VerificationToken testToken;
//
//    @BeforeEach
//    void setUp() {
//        testUser = User.builder()
//                .email("test@example.com")
//                .passwordHash("encodedPassword")
//                .firstName("John")
//                .lastName("Doe")
//                .role(UserRole.CUSTOMER)
//                .isEmailVerified(false)
//                .isActive(false)
//                .build();
//
//        testToken = VerificationToken.builder()
//                .token("test-token-123")
//                .user(testUser)
//                .expiryDate(LocalDateTime.now().plusHours(24))
//                .build();
//    }
//
//    @Test
//    void findByToken_WhenTokenExists_ShouldReturnToken() {
//        entityManager.persist(testUser);
//        entityManager.persist(testToken);
//        entityManager.flush();
//
//        Optional<VerificationToken> found = tokenRepository.findByToken("test-token-123");
//
//        assertTrue(found.isPresent());
//        assertEquals("test-token-123", found.get().getToken());
//    }
//
//    @Test
//    void findByToken_WhenTokenNotExists_ShouldReturnEmpty() {
//        Optional<VerificationToken> found = tokenRepository.findByToken("nonexistent-token");
//
//        assertFalse(found.isPresent());
//    }
//
//    @Test
//    void findByUser_WhenUserHasToken_ShouldReturnToken() {
//        entityManager.persist(testUser);
//        entityManager.persist(testToken);
//        entityManager.flush();
//
//        Optional<VerificationToken> found = tokenRepository.findByUser(testUser);
//
//        assertTrue(found.isPresent());
//        assertEquals(testToken.getToken(), found.get().getToken());
//    }
//
//    @Test
//    void findByUser_WhenUserHasNoToken_ShouldReturnEmpty() {
//        entityManager.persist(testUser);
//        entityManager.flush();
//
//        Optional<VerificationToken> found = tokenRepository.findByUser(testUser);
//
//        assertFalse(found.isPresent());
//    }
//
//    @Test
//    void save_WithValidToken_ShouldPersistToken() {
//        entityManager.persist(testUser);
//        entityManager.flush();
//
//        VerificationToken saved = tokenRepository.save(testToken);
//
//        assertNotNull(saved.getId());
//        assertEquals("test-token-123", saved.getToken());
//    }
//
//    @Test
//    void delete_WhenTokenExists_ShouldDeleteToken() {
//        entityManager.persist(testUser);
//        entityManager.persist(testToken);
//        entityManager.flush();
//
//        tokenRepository.delete(testToken);
//
//        Optional<VerificationToken> found = tokenRepository.findByToken("test-token-123");
//        assertFalse(found.isPresent());
//    }
//
//    @Test
//    void deleteById_WhenTokenExists_ShouldDeleteToken() {
//        entityManager.persist(testUser);
//        entityManager.persist(testToken);
//        entityManager.flush();
//
//        tokenRepository.deleteById(testToken.getId());
//
//        Optional<VerificationToken> found = tokenRepository.findById(testToken.getId());
//        assertFalse(found.isPresent());
//    }
//
//    @Test
//    void isExpired_WhenTokenIsExpired_ShouldReturnTrue() {
//        testToken.setExpiryDate(LocalDateTime.now().minusHours(1));
//        entityManager.persist(testUser);
//        entityManager.persist(testToken);
//        entityManager.flush();
//
//        Optional<VerificationToken> found = tokenRepository.findByToken("test-token-123");
//        assertTrue(found.isPresent());
//        assertTrue(found.get().isExpired());
//    }
//
//    @Test
//    void isExpired_WhenTokenIsNotExpired_ShouldReturnFalse() {
//        testToken.setExpiryDate(LocalDateTime.now().plusHours(1));
//        entityManager.persist(testUser);
//        entityManager.persist(testToken);
//        entityManager.flush();
//
//        Optional<VerificationToken> found = tokenRepository.findByToken("test-token-123");
//        assertTrue(found.isPresent());
//        assertFalse(found.get().isExpired());
//    }
//}
