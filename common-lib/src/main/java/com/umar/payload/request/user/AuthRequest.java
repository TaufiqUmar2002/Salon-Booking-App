package com.umar.payload.request.user;

import com.umar.payload.constants.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Size(max = 12,min =6)
    private String phone;

    @NotNull
    private UserRole role;

}
