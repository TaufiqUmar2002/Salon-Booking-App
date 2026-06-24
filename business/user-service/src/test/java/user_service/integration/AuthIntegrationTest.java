//package user_service.integration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.umar.payload.request.user.AuthRequest;
//import com.umar.payload.request.user.LoginRequest;
//import com.umar.payload.response.user.AuthResponse;
//import com.umar.payload.response.user.LoginResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//import user_service.constants.UserRole;
//import user_service.model.User;
//import user_service.repository.UserRepository;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class AuthIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp() {
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void completeAuthFlow_SignupLoginRefresh_ShouldWorkEndToEnd() throws Exception {
//        AuthRequest authRequest = AuthRequest.builder()
//                .email("integration@example.com")
//                .password("password123")
//                .role(UserRole.CUSTOMER)
//                .firstName("Integration")
//                .lastName("Test")
//                .build();
//
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userId").exists())
//                .andExpect(jsonPath("$.email").value("integration@example.com"));
//
//        User user = userRepository.findByEmail("integration@example.com").orElseThrow();
//        user.setIsEmailVerified(true);
//        user.setIsActive(true);
//        userRepository.save(user);
//
//        LoginRequest loginRequest = new LoginRequest("integration@example.com", "password123");
//
//        String loginResponse = mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").exists())
//                .andExpect(jsonPath("$.refreshToken").exists())
//                .andExpect(jsonPath("$.tokenType").value("Bearer"))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        LoginResponse response = objectMapper.readValue(loginResponse, LoginResponse.class);
//
//        mockMvc.perform(post("/auth/refresh-token")
//                        .param("refresh-token", response.getRefreshToken()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").exists())
//                .andExpect(jsonPath("$.refreshToken").exists());
//    }
//
//    @Test
//    void signup_WithDuplicateEmail_ShouldReturnConflict() throws Exception {
//        AuthRequest authRequest = AuthRequest.builder()
//                .email("duplicate@example.com")
//                .password("password123")
//                .role(UserRole.CUSTOMER)
//                .firstName("John")
//                .lastName("Doe")
//                .build();
//
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
//        AuthRequest authRequest = AuthRequest.builder()
//                .email("login@example.com")
//                .password("password123")
//                .role(UserRole.CUSTOMER)
//                .firstName("Login")
//                .lastName("Test")
//                .build();
//
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(status().isCreated());
//
//        User user = userRepository.findByEmail("login@example.com").orElseThrow();
//        user.setIsEmailVerified(true);
//        user.setIsActive(true);
//        userRepository.save(user);
//
//        LoginRequest loginRequest = new LoginRequest("login@example.com", "wrongpassword");
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void login_WithUnverifiedEmail_ShouldReturnForbidden() throws Exception {
//        AuthRequest authRequest = AuthRequest.builder()
//                .email("unverified@example.com")
//                .password("password123")
//                .role(UserRole.CUSTOMER)
//                .firstName("Unverified")
//                .lastName("Test")
//                .build();
//
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(status().isCreated());
//
//        LoginRequest loginRequest = new LoginRequest("unverified@example.com", "password123");
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isForbidden());
//    }
//}
