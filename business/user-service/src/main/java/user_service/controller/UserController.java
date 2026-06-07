package user_service.controller;


import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.user.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import user_service.model.CustomUserDetails;
import user_service.service.UserService;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/profile/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or #id == authentication.principal.id"
    )
    public ResponseEntity<UserProfileResponse> viewUserProfile(@PathVariable("id")Long id){
        UserProfileResponse response = userService.getUserProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserProfileResponse>> getAllUsersForAdmin(@RequestParam(name = "page",required = false) int page,@RequestParam(name = "size",required = false) int size){
        Page<UserProfileResponse> userProfileResponseList = this.userService.getAllUsersForAdmin(page,size);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResponseList);
    }

    @PutMapping("/profile/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or #id == authentication.principal.id"
    )
    public ResponseEntity<UserProfileResponse> updateUserProfile(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRequest request){
        UserProfileResponse profileResponse = userService.updateUserProfile(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
    }

    @PatchMapping("/profile/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<Void> updateProfilePicture(@PathVariable("id") Long id, @RequestParam("file")MultipartFile file){
         userService.updateProfilePhoto(id,file);
         return ResponseEntity.noContent().build();
    }

    @PatchMapping("/notifications/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<Void> updateNotificationChannel(@PathVariable("id") Long id,@Valid @RequestBody UserNotificationRequest request){
         userService.updateUserNotification(id,request);
         return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequest request, @AuthenticationPrincipal CustomUserDetails principal, HttpServletRequest servletRequest){
        String accessToken = this.getAccessTokenFromRequestHeader(servletRequest);
        userService.logoutUser(principal.getId(),request,accessToken);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id")Long id, @RequestBody DeleteUserRequest request,HttpServletRequest servletRequest){
        String accessToken = this.getAccessTokenFromRequestHeader(servletRequest);
        userService.deleteUser(id,request!=null?request.getReason():null,accessToken);
        return ResponseEntity.ok(Map.of("message", "user deleted successfully"));
    }

    @GetMapping("/validate")
    public ResponseEntity<UserValidateResponse> validateUser(HttpServletRequest request){
        String accessToken = this.getAccessTokenFromRequestHeader(request);
        UserValidateResponse response = userService.validateUser(accessToken);
        return ResponseEntity.ok(response);
    }


    public String getAccessTokenFromRequestHeader(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            throw new ApiException(HttpStatus.NOT_FOUND,"NO_TOKEN_FOUND","No token found in request");
        }
        return authHeader.substring(7);
    }

}
