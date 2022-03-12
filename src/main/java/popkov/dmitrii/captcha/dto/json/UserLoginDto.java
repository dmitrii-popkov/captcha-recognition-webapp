package popkov.dmitrii.captcha.dto.json;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import popkov.dmitrii.captcha.dto.error.OnSignIn;
import popkov.dmitrii.captcha.dto.error.OnSignUp;
import popkov.dmitrii.captcha.validation.LoginUniqueValidator;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank(groups = {OnSignUp.class, OnSignIn.class})
    @NotNull(groups = {OnSignUp.class, OnSignIn.class})
    @LoginUniqueValidator(groups = OnSignUp.class)
    private String login;
    @Size(min = 3, max = 26, groups = {OnSignUp.class, OnSignIn.class})
    @NotNull(groups = {OnSignUp.class, OnSignIn.class})
    private String password;
}
