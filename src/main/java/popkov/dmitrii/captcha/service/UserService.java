package popkov.dmitrii.captcha.service;

import popkov.dmitrii.captcha.dto.json.UserDto;
import popkov.dmitrii.captcha.dto.json.UserLoginDto;
import popkov.dmitrii.captcha.entity.user.RoleType;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.mapper.UserDtoEntityMapper;
import popkov.dmitrii.captcha.repository.user.UserRepository;
import popkov.dmitrii.captcha.repository.user.UserRoleRepository;
import popkov.dmitrii.captcha.security.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDtoEntityMapper userDtoEntityMapper;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUser(String token) {
        String login = tokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByLogin(login);
        return userDtoEntityMapper.entityToDto(user);
    }

    public UserDto createUser(UserLoginDto userDto) {
        User user = userDtoEntityMapper.dtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(userRoleRepository.findByRoleType(RoleType.USER));
        userRepository.save(user);

        userDetailsService.loadUserByUsername(userDto.getLogin());
        return userDtoEntityMapper.entityToDto(user);
    }
}
