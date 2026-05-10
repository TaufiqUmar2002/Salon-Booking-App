package com.umar.payload.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String accessTokenExpiry;
    private Long userId;
    private String email;
    private String role;
    private String firstName;
}
