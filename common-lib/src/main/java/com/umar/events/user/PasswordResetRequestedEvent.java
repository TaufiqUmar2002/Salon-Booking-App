package com.umar.events.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PasswordResetRequestedEvent {
    private String userId;
    private String email;
    private String resetToken;
}
