package com.umar.payload.request.user;

import com.umar.payload.constants.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&].*$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character"
    )
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Size(max = 12,min =6)
    @Pattern(
            regexp = "^\\+[1-9]\\d{1,14}$",
            message = "Phone number must match E.164 format (e.g., +15550199)"
    )
    private String phone;

    @NotNull
    private UserRole role;

}
