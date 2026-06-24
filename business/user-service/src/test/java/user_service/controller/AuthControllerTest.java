//package user_service.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.umar.payload.request.user.AuthRequest;
//import com.umar.payload.request.user.ForgotPasswordRequest;
//import com.umar.payload.request.user.LoginRequest;
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
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import user_service.constants.UserRole;
//import user_service.service.AuthService;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class AuthControllerTest {
//
//    @Mock
//    private AuthService authService;
//
//    @InjectMocks
//    private AuthController authController;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    private AuthRequest authRequest;
//    private LoginRequest loginRequest;
//    private ForgotPasswordRequest forgotPasswordRequest;
//    private ResetPasswordRequest resetPasswordRequest;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
//        objectMapper = new ObjectMapper();
//
//        authRequest = AuthRequest.builder()
//                .email("test@example.com")
//                .password("password123")
//                .role(UserRole.CUSTOMER)
//                .firstName("John")
//                .lastName("Doe")
//                .build();
//
//        loginRequest = new LoginRequest("test@example.com", "password123");
//
//        forgotPasswordRequest = new ForgotPasswordRequest("test@example.com");
//
//        resetPasswordRequest = new ResetPasswordRequest("token123", "newPassword123");
//    }
//
//    @Test
//    void signUp_WithValidRequest_ShouldReturnCreated() throws Exception {
//        AuthResponse authResponse = AuthResponse.builder()
//                .userId(1L)
//                .email("test@example.com")
//                .firstname("John")
//                .message("registration.success.message")
//                .build();
//
//        when(authService.signUp(any(AuthRequest.class))).thenReturn(authResponse);
//
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.email").value("test@example.com"))
//                .andExpect(jsonPath("$.firstname").value("John"));
//    }
//
//    @Test
//    void login_WithValidCredentials_ShouldReturnOk() throws Exception {
//        LoginResponse loginResponse = LoginResponse.builder()
//                .accessToken("accessToken")
//                .refreshToken("refreshToken")
//                .tokenType("Bearer")
//                .userId(1L)
//                .accessTokenExpiry("expiryDate")
//                .firstName("John")
//                .email("test@example.com")
//                .role(UserRole.CUSTOMER.name())
//                .build();
//
//        when(authService.login(anyString(), anyString(), anyString())).thenReturn(loginResponse);
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").value("accessToken"))
//                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
//                .andExpect(jsonPath("$.tokenType").value("Bearer"));
//    }
//
//    @Test
//    void refreshToken_WithValidToken_ShouldReturnOk() throws Exception {
//        RefreshTokenResponse response = RefreshTokenResponse.builder()
//                .accessToken("newAccessToken")
//                .refreshToken("newRefreshToken")
//                .accessTokenExpiry("expiryDate")
//                .build();
//
//        when(authService.refreshToken(anyString())).thenReturn(response);
//
//        mockMvc.perform(post("/auth/refresh-token")
//                        .param("refresh-token", "validRefreshToken"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
//                .andExpect(jsonPath("$.refreshToken").value("newRefreshToken"));
//    }
//
//    @Test
//    void verifyEmail_WithValidToken_ShouldReturnOk() throws Exception {
//        mockMvc.perform(get("/auth/verify-email")
//                        .param("verification-token", "validToken"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void forgotPassword_WithValidEmail_ShouldReturnOk() throws Exception {
//        ForgotEmailResponse response = ForgotEmailResponse.builder()
//                .message("forgotEmail.success.message")
//                .build();
//
//        when(authService.forgotEmail(any(ForgotPasswordRequest.class))).thenReturn(response);
//
//        mockMvc.perform(post("/auth/forgot-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(forgotPasswordRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("forgotEmail.success.message"));
//    }
//
//    @Test
//    void resetPassword_WithValidToken_ShouldReturnNoContent() throws Exception {
//        mockMvc.perform(post("/auth/reset-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
//                .andExpect(status().isNoContent());
//    }
//}
