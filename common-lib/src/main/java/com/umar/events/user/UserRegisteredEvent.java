package com.umar.events.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent implements Serializable {

    private Long userId;
    private String email;
    private String firstName;
    private String role;
    private String verificationToken;
    private LocalDateTime producedAt;

}
