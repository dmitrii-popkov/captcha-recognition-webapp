package popkov.dmitrii.captcha.dto.json;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryNoteDto {
    private String action;
    private String user;
    private Date stamp;
    private CaptchaDto captcha;
    private String tag;
    private String description;
}
