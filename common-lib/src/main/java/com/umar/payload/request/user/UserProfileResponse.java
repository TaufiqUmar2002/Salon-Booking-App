package com.umar.payload.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private String profilePhotoUrl;
    private Boolean notifyEmail;
    private Boolean notifySms;
    private Boolean notifyPush;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
