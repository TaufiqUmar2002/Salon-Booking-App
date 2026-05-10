package user_service.service;

import com.umar.events.user.UserProfileUpdatedEvent;
import com.umar.events.user.UserRegisteredEvent;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.user.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import user_service.constants.UserRole;
import user_service.events.UserEventProducer;
import user_service.file.FileStorageService;
import user_service.mapper.UserMapper;
import user_service.model.User;
import user_service.model.UserAuditLog;
import user_service.model.VerificationToken;
import user_service.repository.UserAuditLogRepository;
import user_service.repository.UserRepository;
import user_service.serviceInterface.IUserService;
import user_service.serviceInterface.IVerificationService;
import user_service.util.JwtUtil;
import user_service.util.TokenBlacklistService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final IVerificationService verificationService;
    private final UserEventProducer eventProducer;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final JwtUtil jwtProvider;
    private final TokenBlacklistService blacklistService;
    private final UserAuditLogRepository auditLogRepository;

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        if(userOptional.isEmpty()){
            throw new ApiException(HttpStatus.NOT_FOUND,"USER_NOT_FOUND","User not found");
        }
        return userMapper.toResponse(userOptional.get());
    }

    @Override
    public UserProfileResponse updateUserProfile(Long userId, UpdateUserRequest request) {
        UserRegisteredEvent registeredEvent=null;
        List<String> updatedFields = new ArrayList<>();
        User user = this.validateUserExists(userId, null);
        if(request.getEmail()!=null && !request.getEmail().equals(user.getEmail())){
            user.setIsEmailVerified(false);
            updateField(request.getEmail(),user.getEmail(),user::setEmail,"email",updatedFields);
            VerificationToken verificationToken = verificationService.createToken(user);
            registeredEvent = UserRegisteredEvent.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .producedAt(LocalDateTime.now())
                    .role(user.getRole().name())
                    .userId(user.getId())
                    .build();
        }
        updateField(request.getLastname(),user.getLastName(),user::setLastName,"lastName",updatedFields);
        updateField(request.getFirstName(),user.getFirstName(),user::setLastName,"firstName",updatedFields);
        updateField(request.getPhone(),user.getPhone(),user::setPhone,"phone",updatedFields);
        if(request.getRole()!=null &&request.getRole().equals("ADMIN")){
            throw new ApiException(HttpStatus.FORBIDDEN,"INVALID_ROLE","ADMIN role cannot be self-assigned");
        }
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(UserRole.CUSTOMER);
        userRepository.save(user);
        if(registeredEvent!=null){
            eventProducer.publishUserRegisteredEvent(registeredEvent);
        }
        UserProfileUpdatedEvent event = UserProfileUpdatedEvent.builder()
                        .userId(user.getId())
                                .updatedAt(LocalDateTime.now())
                                        .updatedFields(updatedFields).build();
        eventProducer.publishUserProfileUpdateEvent(event);
        return userMapper.toResponse(user);
    }

    @Override
    public void updateProfilePhoto(Long userId, MultipartFile file) {
        User user =validateUserExists(userId,null);
        this.validateImage(file);
        String imageUrl = fileStorageService.upload(file);
        user.setProfilePhotoUrl(imageUrl);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void updateUserNotification(Long id,UserNotificationRequest request) {
        User user = this.validateUserExists(id,null);
        if(request==null){
            throw new ApiException(HttpStatus.BAD_REQUEST,"NO_FIELDS_PROVIDED","'At least one notification preference must be provided'");
        }
        updateField(request.getNotifyEmail(),user.getNotifyEmail(),user::setNotifyEmail,"notifyEmail",null);
        updateField(request.getNotifyPush(),user.getNotifyPush(),user::setNotifyPush,"notifyPush",null);
        updateField(request.getNotifySms(),user.getNotifySms(),user::setNotifySms,"notifyEmail",null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void logoutUser(Long userId, LogoutRequest request,String accessToken) {
        User user =validateUserExists(userId, null);
        if(request == null || request.getRefreshToken() == null || request.getRefreshToken().isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"MISSING_TOKEN","Refresh token is required for logout");
        }
        if(!user.getRefreshToken().equals(request.getRefreshToken())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"","");
        }
        user.setRefreshToken(null);
        userRepository.save(user);
        long remainingExpiry = jwtProvider.getRemainingExpiration(accessToken);
        blacklistService.blacklistToken(accessToken,remainingExpiry);


    }

    @Override
    public void deleteUser(Long id,String reason,String accessToken){
        User user =validateUserExists(id,null);
        if(!user.getIsActive()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"ALREADY_INACTIVE","This account is already inactive");
        }
        user.setRefreshToken(null);
        user.setIsActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        String email = jwtProvider.extractUserEmail(accessToken);
        User adminUser = validateUserExists(null,email);
        if(Objects.equals(user.getId(), adminUser.getId())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"SELF_DEACTIVATION","You cannot deactivate your own account");
        }
        UserAuditLog auditLog = UserAuditLog.builder()
                .targetUserId(user.getId())
                .reason(reason)
                .adminId(adminUser.getId())
                .timeStamp(LocalDateTime.now()).build();
        auditLogRepository.save(auditLog);

    }

    @Override
    public UserValidateResponse validateUser(String accessToken) {
        if(jwtProvider.isTokenExpired(accessToken)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"","");
        }
        Claims claims = jwtProvider.extractClaims(accessToken);
        Long userId = claims.get("userId",Long.class);
        String email = claims.getSubject();
        String role = claims.get("role",String.class);
        return UserValidateResponse.builder()
                .userId(userId)
                .email(email)
                .role(role).build();
    }


    private void validateImage(MultipartFile file){
        if(file.isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"EMPTY_FILE","Image file is required");
        }
        List<String> allowedTypes = List.of("image/jpeg","image/png","image/webp");
        if(!allowedTypes.contains(file.getContentType())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_FILE_TYPE","Only JPG, PNG, WEBP allowed");
        }
        long maxSize = 5 * 1024 * 1024;
        if(file.getSize()>maxSize){
            throw new ApiException(HttpStatus.BAD_REQUEST,"FILE_TOO_LARGE","Max file size is 5MB");
        }
    }

    private <T> void updateField(T newValue, T oldValue, Consumer<T> setter, String fieldName, List<String> updatedFields) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            updatedFields.add(fieldName);
        }
    }
    private User validateUserExists(Long userId,String email){
        User user;
        if(userId!=null){
            Optional<User> userOptional = repository.findById(userId);
            if(userOptional.isEmpty()) {
                throw new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found");
            }
            user=userOptional.get();
        }
        else {
            Optional<User> userOptional = repository.findByEmail(email);
            if(userOptional.isEmpty()){
                throw new ApiException(HttpStatus.NOT_FOUND,"USER_NOT_FOUND","User not found");
            }
            user=userOptional.get();
        }
        return user;
    }


}
