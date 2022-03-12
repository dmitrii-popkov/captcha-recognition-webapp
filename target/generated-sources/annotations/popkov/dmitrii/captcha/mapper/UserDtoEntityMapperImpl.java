package popkov.dmitrii.captcha.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import popkov.dmitrii.captcha.dto.json.UserDto;
import popkov.dmitrii.captcha.dto.json.UserLoginDto;
import popkov.dmitrii.captcha.entity.user.RoleType;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.entity.user.User.UserBuilder;
import popkov.dmitrii.captcha.entity.user.UserRole;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-12T21:51:30+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.9 (JetBrains s.r.o.)"
)
@Component
public class UserDtoEntityMapperImpl extends UserDtoEntityMapper {

    @Override
    public UserDto entityToDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        String role = null;
        String picture = null;
        String login = null;

        RoleType roleType = entityUserRoleRoleType( entity );
        if ( roleType != null ) {
            role = roleType.name();
        }
        picture = entity.getPicturePath();
        login = entity.getLogin();

        UserDto userDto = new UserDto( login, role, picture );

        return userDto;
    }

    @Override
    public User dtoToEntity(UserLoginDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.login( userDto.getLogin() );
        user.password( userDto.getPassword() );

        return user.build();
    }

    private RoleType entityUserRoleRoleType(User user) {
        if ( user == null ) {
            return null;
        }
        UserRole userRole = user.getUserRole();
        if ( userRole == null ) {
            return null;
        }
        RoleType roleType = userRole.getRoleType();
        if ( roleType == null ) {
            return null;
        }
        return roleType;
    }
}
