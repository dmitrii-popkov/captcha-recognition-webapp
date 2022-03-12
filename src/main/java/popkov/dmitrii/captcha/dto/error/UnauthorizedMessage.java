package popkov.dmitrii.captcha.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnauthorizedMessage {
    private String message;
}
