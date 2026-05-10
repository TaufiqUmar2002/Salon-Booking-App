package user_service.mapper;

import com.umar.payload.request.user.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import user_service.model.User;
import com.umar.payload.request.user.AuthRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "passwordHash",ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isEmailVerified", constant = "false")
    User toEntity(AuthRequest request);

    @Mapping(target = "password", source = "passwordHash")
    AuthRequest toDto(User user);

    @Mapping(target = "userId",source = "id")
    UserProfileResponse toResponse(User user);
}
