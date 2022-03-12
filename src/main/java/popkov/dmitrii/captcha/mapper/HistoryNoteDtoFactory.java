package popkov.dmitrii.captcha.mapper;

import java.util.Date;
import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.dto.json.CaptchaTypeDto;
import popkov.dmitrii.captcha.dto.json.HistoryNoteDto;
import popkov.dmitrii.captcha.entity.history.ActionType;
import org.springframework.stereotype.Component;

@Component
public class HistoryNoteDtoFactory {
    
    public HistoryNoteDto createNote(ActionType actionType, String login, CaptchaDto captchaDto) {
        return createTemplate(actionType, login).captcha(captchaDto).build();
    }
    public HistoryNoteDto createNote(ActionType actionType, String login, CaptchaTypeDto captchaTypeDto) {
        return createTemplate(actionType, login).tag(captchaTypeDto.getName()).build();
    }
    
    private HistoryNoteDto.HistoryNoteDtoBuilder createTemplate(ActionType actionType, String login) {
        return HistoryNoteDto.builder().action(actionType.name())
                .user(login)
                .stamp(new Date());
    }
}
