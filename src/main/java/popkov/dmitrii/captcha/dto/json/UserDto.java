package popkov.dmitrii.captcha.dto.json;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private String login;
    private String role;
    private String picture;
}
