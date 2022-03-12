package popkov.dmitrii.captcha.dto.json;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CaptchaTypeDto {
    @NotBlank
    private String name;
    private int count;
}
