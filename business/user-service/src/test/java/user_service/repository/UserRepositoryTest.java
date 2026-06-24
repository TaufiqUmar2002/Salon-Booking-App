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
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@ActiveProfiles("test")
//class UserRepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private User testUser;
//
//    @BeforeEach
//    void setUp() {
//        testUser = User.builder()
//                .email("test@example.com")
//                .passwordHash("encodedPassword")
//                .firstName("John")
//                .lastName("Doe")
//                .role(UserRole.CUSTOMER)
//                .isEmailVerified(true)
//                .isActive(true)
//                .build();
//    }
//
//    @Test
//    void findByEmail_WhenUserExists_ShouldReturnUser() {
//        entityManager.persist(testUser);
//        entityManager.flush();
//
//        Optional<User> found = userRepository.findByEmail("test@example.com");
//
//        assertTrue(found.isPresent());
//        assertEquals("test@example.com", found.get().getEmail());
//    }
//
//    @Test
//    void findByEmail_WhenUserNotExists_ShouldReturnEmpty() {
//        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");
//
//        assertFalse(found.isPresent());
//    }
//
//    @Test
//    void findInactiveUsers_WithInactiveUsers_ShouldReturnList() {
//        testUser.setLastBookingDate(LocalDateTime.now().minusMonths(7));
//        entityManager.persist(testUser);
//
//        User activeUser = User.builder()
//                .email("active@example.com")
//                .passwordHash("encodedPassword")
//                .firstName("Jane")
//                .lastName("Smith")
//                .role(UserRole.CUSTOMER)
//                .isEmailVerified(true)
//                .isActive(true)
//                .lastBookingDate(LocalDateTime.now())
//                .build();
//        entityManager.persist(activeUser);
//
//        entityManager.flush();
//
//        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(6);
//        List<User> inactiveUsers = userRepository.findInactiveUsers(cutoffDate);
//
//        assertEquals(1, inactiveUsers.size());
//        assertEquals("test@example.com", inactiveUsers.get(0).getEmail());
//    }
//
//    @Test
//    void findInactiveUsers_WithNoInactiveUsers_ShouldReturnEmptyList() {
//        testUser.setLastBookingDate(LocalDateTime.now());
//        entityManager.persist(testUser);
//        entityManager.flush();
//
//        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(6);
//        List<User> inactiveUsers = userRepository.findInactiveUsers(cutoffDate);
//
//        assertTrue(inactiveUsers.isEmpty());
//    }
//
//    @Test
//    void save_WithValidUser_ShouldPersistUser() {
//        User saved = userRepository.save(testUser);
//
//        assertNotNull(saved.getId());
//        assertEquals("test@example.com", saved.getEmail());
//    }
//
//    @Test
//    void findById_WhenUserExists_ShouldReturnUser() {
//        User saved = userRepository.save(testUser);
//
//        Optional<User> found = userRepository.findById(saved.getId());
//
//        assertTrue(found.isPresent());
//        assertEquals(saved.getId(), found.get().getId());
//    }
//
//    @Test
//    void deleteById_WhenUserExists_ShouldDeleteUser() {
//        User saved = userRepository.save(testUser);
//
//        userRepository.deleteById(saved.getId());
//
//        Optional<User> found = userRepository.findById(saved.getId());
//        assertFalse(found.isPresent());
//    }
//}
