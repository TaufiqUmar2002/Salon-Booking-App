package user_service.model;



import jakarta.persistence.*;
import lombok.*;
import user_service.constants.UserRole;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users_salon")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean isEmailVerified;

    private Boolean isActive;

    private String profilePhotoUrl;

    private Boolean notifyEmail;

    private Boolean notifySms;

    private Boolean notifyPush;

    private String passwordResetToken;

    private String refreshToken;

    private LocalDateTime lastBookingDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

}
