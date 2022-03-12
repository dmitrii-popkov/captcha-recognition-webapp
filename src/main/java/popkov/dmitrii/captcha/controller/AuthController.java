package popkov.dmitrii.captcha.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import popkov.dmitrii.captcha.dto.error.OnSignIn;
import popkov.dmitrii.captcha.dto.error.OnSignUp;
import popkov.dmitrii.captcha.dto.json.UserDto;
import popkov.dmitrii.captcha.dto.json.UserLoginDto;
import popkov.dmitrii.captcha.entity.user.RoleType;
import popkov.dmitrii.captcha.security.AuthToken;
import popkov.dmitrii.captcha.security.TokenProvider;
import popkov.dmitrii.captcha.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Validated(OnSignIn.class)
    @PostMapping
    public ResponseEntity<UserDto> authenticate(@Valid @RequestBody UserLoginDto user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getLogin(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        AuthToken authToken = new AuthToken(token);
        ResponseCookie springCookie = ResponseCookie.from("token", authToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(900)
                .domain("localhost")
                .build();
        UserDto userDto = userService.getUser(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(userDto);
    }

    @Validated(OnSignUp.class)
    @PostMapping("/create")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserLoginDto userDto) {
        userService.createUser(userDto);
        return authenticate(userDto);
    }
    @GetMapping
    public ResponseEntity<UserDto> getUserInfo(@CookieValue(value = "token", defaultValue = "") String token) {
        if (token.isEmpty()) {
            return ResponseEntity.status(401).body(new UserDto("anonymous", RoleType.ANONYMOUS.name(), ""));
        }
        return ResponseEntity.ok(userService.getUser(token));
    }
    @DeleteMapping
    public ResponseEntity<UserDto> logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie tokenCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .domain("localhost")
                .build();
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, tokenCookie.toString()).build();
    }
}
