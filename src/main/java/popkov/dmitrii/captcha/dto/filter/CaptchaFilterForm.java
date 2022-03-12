package popkov.dmitrii.captcha.dto.filter;

import lombok.Data;

@Data
public class CaptchaFilterForm {
    private String tag;
    private String name;
    private CaptchaFilter filter;
    private String user;
}
