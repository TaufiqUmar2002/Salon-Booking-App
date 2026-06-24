//package user_service.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.umar.payload.request.user.DeleteUserRequest;
//import com.umar.payload.request.user.LogoutRequest;
//import com.umar.payload.request.user.UpdateUserRequest;
//import com.umar.payload.request.user.UserNotificationRequest;
//import com.umar.payload.response.user.UserProfileResponse;
//import com.umar.payload.response.user.UserValidateResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import user_service.model.CustomUserDetails;
//import user_service.service.UserService;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private Authentication authentication;
//
//    @InjectMocks
//    private UserController userController;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    private UserProfileResponse userProfileResponse;
//    private UpdateUserRequest updateUserRequest;
//    private UserNotificationRequest notificationRequest;
//    private LogoutRequest logoutRequest;
//    private DeleteUserRequest deleteUserRequest;
//    private CustomUserDetails customUserDetails;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//        objectMapper = new ObjectMapper();
//
//        userProfileResponse = UserProfileResponse.builder()
//                .userId(1L)
//                .email("test@example.com")
//                .firstName("John")
//                .lastName("Doe")
//                .role("CUSTOMER")
//                .build();
//
//        updateUserRequest = new UpdateUserRequest();
//        updateUserRequest.setFirstName("Jane");
//        updateUserRequest.setLastname("Smith");
//        updateUserRequest.setEmail("jane@example.com");
//
//        notificationRequest = new UserNotificationRequest();
//        notificationRequest.setNotifyEmail(true);
//        notificationRequest.setNotifyPush(false);
//        notificationRequest.setNotifySms(true);
//
//        logoutRequest = new LogoutRequest();
//        logoutRequest.setRefreshToken("refreshToken123");
//
//        deleteUserRequest = new DeleteUserRequest();
//        deleteUserRequest.setReason("Violation of terms");
//
//        customUserDetails = new CustomUserDetails(1L, "test@example.com", "password", Collections.emptyList());
//    }
//
//    @Test
//    void viewUserProfile_WithValidId_ShouldReturnUserProfile() throws Exception {
//        when(userService.getUserProfile(1L)).thenReturn(userProfileResponse);
//
//        mockMvc.perform(get("/api/users/profile/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.email").value("test@example.com"))
//                .andExpect(jsonPath("$.firstName").value("John"));
//    }
//
//    @Test
//    void getAllUsersForAdmin_ShouldReturnPageOfUsers() throws Exception {
//        Page<UserProfileResponse> page = new PageImpl<>(Collections.singletonList(userProfileResponse));
//        when(userService.getAllUsersForAdmin(0, 10)).thenReturn(page);
//
//        mockMvc.perform(get("/api/users")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].userId").value(1L));
//    }
//
//    @Test
//    void updateUserProfile_WithValidRequest_ShouldReturnUpdatedProfile() throws Exception {
//        when(userService.updateUserProfile(eq(1L), any(UpdateUserRequest.class))).thenReturn(userProfileResponse);
//
//        mockMvc.perform(put("/api/users/profile/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateUserRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value(1L));
//    }
//
//    @Test
//    void updateProfilePicture_WithValidFile_ShouldReturnNoContent() throws Exception {
//        mockMvc.perform(patch("/api/users/profile/1")
//                        .param("file", "file"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void updateNotificationChannel_WithValidRequest_ShouldReturnNoContent() throws Exception {
//        mockMvc.perform(patch("/api/users/notifications/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(notificationRequest)))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void logout_WithValidRequest_ShouldReturnOk() throws Exception {
//        when(authentication.getPrincipal()).thenReturn(customUserDetails);
//
//        mockMvc.perform(post("/api/users/logout")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(logoutRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Logged out successfully"));
//    }
//
//    @Test
//    void deleteUser_WithValidRequest_ShouldReturnOk() throws Exception {
//        when(authentication.getPrincipal()).thenReturn(customUserDetails);
//
//        mockMvc.perform(delete("/api/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(deleteUserRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("user deleted successfully"));
//    }
//
//    @Test
//    void validateUser_WithValidToken_ShouldReturnUserDetails() throws Exception {
//        UserValidateResponse response = UserValidateResponse.builder()
//                .userId(1L)
//                .email("test@example.com")
//                .role("CUSTOMER")
//                .build();
//
//        when(userService.validateUser(anyString())).thenReturn(response);
//
//        mockMvc.perform(get("/api/users/validate")
//                        .header("Authorization", "Bearer accessToken"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.email").value("test@example.com"))
//                .andExpect(jsonPath("$.role").value("CUSTOMER"));
//    }
//}
