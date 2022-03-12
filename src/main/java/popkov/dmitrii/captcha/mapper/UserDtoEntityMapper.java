package popkov.dmitrii.captcha.mapper;

import popkov.dmitrii.captcha.dto.json.UserDto;
import popkov.dmitrii.captcha.dto.json.UserLoginDto;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.repository.user.UserRoleRepository;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Setter(onMethod = @__(@Autowired))
@Mapper(componentModel = "spring")
public abstract class UserDtoEntityMapper {
    protected UserRoleRepository userRoleRepository;

    @Mapping(target = "role", source = "entity.userRole.roleType")
    @Mapping(target = "picture", source = "entity.picturePath")
    public abstract UserDto entityToDto(User entity);

    public abstract User dtoToEntity(UserLoginDto userDto);

}
