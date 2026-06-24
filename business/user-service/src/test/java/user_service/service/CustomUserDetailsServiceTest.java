//package user_service.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import user_service.constants.UserRole;
//import user_service.model.CustomUserDetails;
//import user_service.model.User;
//import user_service.repository.UserRepository;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class CustomUserDetailsServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private CustomUserDetailsService customUserDetailsService;
//
//    private User testUser;
//
//    @BeforeEach
//    void setUp() {
//        testUser = User.builder()
//                .id(1L)
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
//    void loadUserByUsername_WhenUserExists_ShouldReturnCustomUserDetails() {
//        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
//
//        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");
//
//        assertNotNull(userDetails);
//        assertEquals(testUser.getId(), userDetails.getId());
//        assertEquals(testUser.getEmail(), userDetails.getUsername());
//        assertEquals(testUser.getPasswordHash(), userDetails.getPassword());
//        assertEquals(1, userDetails.getAuthorities().size());
//        assertTrue(userDetails.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER")));
//    }
//
//    @Test
//    void loadUserByUsername_WhenUserNotExists_ShouldThrowUsernameNotFoundException() {
//        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class, () -> {
//            customUserDetailsService.loadUserByUsername("nonexistent@example.com");
//        });
//    }
//
//    @Test
//    void loadUserByUsername_WithAdminRole_ShouldReturnAdminAuthority() {
//        testUser.setRole(UserRole.ADMIN);
//        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
//
//        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername("admin@example.com");
//
//        assertNotNull(userDetails);
//        assertTrue(userDetails.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
//    }
//
//    @Test
//    void loadUserByUsername_WithSalonOwnerRole_ShouldReturnSalonOwnerAuthority() {
//        testUser.setRole(UserRole.SALON_OWNER);
//        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(testUser));
//
//        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername("owner@example.com");
//
//        assertNotNull(userDetails);
//        assertTrue(userDetails.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SALON_OWNER")));
//    }
//}
