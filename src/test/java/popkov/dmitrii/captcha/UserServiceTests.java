package popkov.dmitrii.captcha;

import popkov.dmitrii.captcha.dto.json.UserDto;
import popkov.dmitrii.captcha.dto.json.UserLoginDto;
import popkov.dmitrii.captcha.entity.user.RoleType;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.entity.user.UserRole;
import popkov.dmitrii.captcha.exception.ApplicationException;
import popkov.dmitrii.captcha.mapper.UserDtoEntityMapperImpl;
import popkov.dmitrii.captcha.repository.user.UserRepository;
import popkov.dmitrii.captcha.repository.user.UserRoleRepository;
import popkov.dmitrii.captcha.security.TokenProvider;
import popkov.dmitrii.captcha.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Spy
    private UserDtoEntityMapperImpl userDtoEntityMapper;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void getUser_whenTokenValidAndUserExists_thenReturnUser() {
        User givenUser = User.builder().login("john").picturePath("abc")
                .userRole(UserRole.builder().roleType(RoleType.USER).build()).build();
        UserDto expectedDto = new UserDto("john", "USER", "abc");

        when(userRepository.findByLogin(any())).thenReturn(givenUser);

        UserDto actualDto = userService.getUser(any());

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void getUser_whenTokenValidAndUserDoesNotExist_thenReturnNull() {
        when(userRepository.findByLogin(any())).thenReturn(null);

        UserDto actualDto = userService.getUser(any());
        assertNull(actualDto);
    }

    @Test
    void getUser_whenTokenInvalid_thenThrowException() {
        assertThrows(ApplicationException.class, () -> {
            when(tokenProvider.validateToken(any())).thenThrow(ApplicationException.class);
            when(tokenProvider.getUsernameFromToken(any())).thenCallRealMethod();
            userService.getUser(any());
        });
    }

    @Test
    void createUser_whenDtoGiven_thenSaveUserAndLoadUserDetails() {
        UserLoginDto givenDto = new UserLoginDto();
        givenDto.setLogin("john");
        givenDto.setPassword("doe");

        UserDto expectedDto = new UserDto("john", "USER", "");
        User expectedUser = User.builder().login("john").picturePath("").build();
        when(userDtoEntityMapper.dtoToEntity(givenDto)).thenReturn(expectedUser);
        when(passwordEncoder.encode(any())).thenReturn("doe");
        when(userRoleRepository.findByRoleType(any()))
                .thenReturn(UserRole.builder().roleType(RoleType.USER).build());

        UserDto actualDto = userService.createUser(givenDto);

        assertEquals(expectedDto, actualDto);
    }

}
