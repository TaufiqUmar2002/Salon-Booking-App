package com.umar.payload.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastname;
    private String phone;
    private String email;
    private String role;
}
