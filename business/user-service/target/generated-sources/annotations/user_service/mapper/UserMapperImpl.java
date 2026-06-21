package user_service.mapper;

import com.umar.payload.request.user.AuthRequest;
import com.umar.payload.request.user.UserProfileResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import user_service.constants.UserRole;
import user_service.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-21T12:53:57+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(AuthRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( request.getEmail() );
        user.firstName( request.getFirstName() );
        user.lastName( request.getLastName() );
        user.phone( request.getPhone() );
        user.role( userRoleToUserRole( request.getRole() ) );

        user.isEmailVerified( false );

        return user.build();
    }

    @Override
    public AuthRequest toDto(User user) {
        if ( user == null ) {
            return null;
        }

        AuthRequest.AuthRequestBuilder authRequest = AuthRequest.builder();

        authRequest.password( user.getPasswordHash() );
        authRequest.email( user.getEmail() );
        authRequest.firstName( user.getFirstName() );
        authRequest.lastName( user.getLastName() );
        authRequest.phone( user.getPhone() );
        authRequest.role( userRoleToUserRole1( user.getRole() ) );

        return authRequest.build();
    }

    @Override
    public UserProfileResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserProfileResponse.UserProfileResponseBuilder userProfileResponse = UserProfileResponse.builder();

        userProfileResponse.userId( user.getId() );
        userProfileResponse.email( user.getEmail() );
        userProfileResponse.firstName( user.getFirstName() );
        userProfileResponse.lastName( user.getLastName() );
        userProfileResponse.phone( user.getPhone() );
        if ( user.getRole() != null ) {
            userProfileResponse.role( user.getRole().name() );
        }
        userProfileResponse.isActive( user.getIsActive() );
        userProfileResponse.isEmailVerified( user.getIsEmailVerified() );
        userProfileResponse.profilePhotoUrl( user.getProfilePhotoUrl() );
        userProfileResponse.notifyEmail( user.getNotifyEmail() );
        userProfileResponse.notifySms( user.getNotifySms() );
        userProfileResponse.notifyPush( user.getNotifyPush() );
        userProfileResponse.createdAt( user.getCreatedAt() );
        userProfileResponse.lastLogin( user.getLastLogin() );

        return userProfileResponse.build();
    }

    protected UserRole userRoleToUserRole(com.umar.payload.constants.UserRole userRole) {
        if ( userRole == null ) {
            return null;
        }

        UserRole userRole1;

        switch ( userRole ) {
            case CUSTOMER: userRole1 = UserRole.CUSTOMER;
            break;
            case SALON_OWNER: userRole1 = UserRole.SALON_OWNER;
            break;
            case STAFF: userRole1 = UserRole.STAFF;
            break;
            case ADMIN: userRole1 = UserRole.ADMIN;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + userRole );
        }

        return userRole1;
    }

    protected com.umar.payload.constants.UserRole userRoleToUserRole1(UserRole userRole) {
        if ( userRole == null ) {
            return null;
        }

        com.umar.payload.constants.UserRole userRole1;

        switch ( userRole ) {
            case CUSTOMER: userRole1 = com.umar.payload.constants.UserRole.CUSTOMER;
            break;
            case SALON_OWNER: userRole1 = com.umar.payload.constants.UserRole.SALON_OWNER;
            break;
            case STAFF: userRole1 = com.umar.payload.constants.UserRole.STAFF;
            break;
            case ADMIN: userRole1 = com.umar.payload.constants.UserRole.ADMIN;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + userRole );
        }

        return userRole1;
    }
}
