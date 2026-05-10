package com.umar.payload.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String accessTokenExpiry;
}
