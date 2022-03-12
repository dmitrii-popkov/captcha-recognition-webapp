package popkov.dmitrii.captcha.service;

import java.util.List;
import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.dto.json.CaptchaTypeDto;
import popkov.dmitrii.captcha.dto.json.HistoryNoteDto;
import popkov.dmitrii.captcha.entity.history.ActionType;
import popkov.dmitrii.captcha.entity.history.HistoryNote;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.mapper.HistoryNoteDtoEntityMapper;
import popkov.dmitrii.captcha.mapper.HistoryNoteDtoFactory;
import popkov.dmitrii.captcha.repository.history.HistoryNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryNoteService {

    private final HistoryNoteRepository historyNoteRepository;
    private final HistoryNoteDtoEntityMapper historyNoteDtoEntityMapper;
    private final HistoryNoteDtoFactory historyNoteDtoFactory;

    @Setter(onMethod = @__(@Autowired))
    private User principal;

    public List<HistoryNoteDto> getNotes() {
        return historyNoteDtoEntityMapper.
                entityToDto(historyNoteRepository.getByUserLogin(principal.getLogin()));
    }

    public HistoryNoteDto addNote(HistoryNoteDto dto) {
        HistoryNote entity = historyNoteDtoEntityMapper.daoToEntity(dto);
        historyNoteRepository.save(entity);
        return historyNoteDtoEntityMapper.entityToDto(entity);
    }
    public HistoryNoteDto addNote(ActionType actionType, CaptchaDto captchaDto) {
        return addNote(historyNoteDtoFactory.createNote(actionType, principal.getLogin(), captchaDto));
    }
    public HistoryNoteDto addNote(ActionType actionType, CaptchaTypeDto captchaTypeDto) {
        return addNote(historyNoteDtoFactory.createNote(actionType, principal.getLogin(), captchaTypeDto));
    }

}
