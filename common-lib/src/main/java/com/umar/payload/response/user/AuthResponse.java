package com.umar.payload.response.user;

import com.umar.payload.enums.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {

    private Long userId;

    private String email;

    private String firstname;

    private UserRole role;

    private String message;

}
