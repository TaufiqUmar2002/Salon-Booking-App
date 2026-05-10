package user_service.serviceInterface;



import com.umar.payload.request.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    UserProfileResponse getUserProfile(Long userId);
    UserProfileResponse updateUserProfile(Long userId, UpdateUserRequest request);
    void updateProfilePhoto(Long userId, MultipartFile file);
    void updateUserNotification(Long id,UserNotificationRequest request);
    void logoutUser(Long userId,LogoutRequest request,String accessToken);
    void deleteUser(Long id,String reason,String accessToken);
    UserValidateResponse validateUser(String accessToken);
}
