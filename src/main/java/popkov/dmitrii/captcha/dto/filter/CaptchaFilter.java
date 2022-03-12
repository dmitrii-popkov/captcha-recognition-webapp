package popkov.dmitrii.captcha.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CaptchaFilter {
    ALL(false, false, false),
    SOLVED(true, false, true),
    NOT_SOLVED(true, false, false),
    UNCHECKED(false, true, false);

    private final boolean checkIsRight;
    private final boolean checkAnswer;
    private final boolean isRight;
}
