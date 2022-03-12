package popkov.dmitrii.captcha;

import java.util.ArrayList;
import java.util.List;
import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.dto.json.CaptchaTypeDto;
import popkov.dmitrii.captcha.dto.json.HistoryNoteDto;
import popkov.dmitrii.captcha.entity.history.ActionType;
import popkov.dmitrii.captcha.entity.history.HistoryNote;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.mapper.HistoryNoteDtoEntityMapperImpl;
import popkov.dmitrii.captcha.mapper.HistoryNoteDtoFactory;
import popkov.dmitrii.captcha.repository.history.HistoryNoteRepository;
import popkov.dmitrii.captcha.service.HistoryNoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryNoteServiceTests {

    @InjectMocks
    @Spy
    private HistoryNoteService historyNoteService;
    @Mock
    private HistoryNoteRepository historyNoteRepository;
    @Mock
    private HistoryNoteDtoEntityMapperImpl historyNoteDtoEntityMapper;
    @Mock
    private HistoryNoteDtoFactory historyNoteDtoFactory;

    @Mock
    private User principal;

    @Test
    void getNotes_whenCalled_thenCallHistoryNoteRepository() {
        String givenLogin = "john";
        when(principal.getLogin()).thenReturn(givenLogin);
        historyNoteService.setPrincipal(principal);
        List<HistoryNote> expectedNotes = new ArrayList<>();

        historyNoteService.getNotes();

        verify(historyNoteDtoEntityMapper).entityToDto(expectedNotes);
        verify(historyNoteRepository).getByUserLogin(givenLogin);

    }
    @Test
    void addNote_whenDtoGiven_thenSaveNoteAndReturnDto() {
        historyNoteService.setPrincipal(principal);
        HistoryNoteDto expectedDto = mock(HistoryNoteDto.class);
        HistoryNoteDto givenDto = mock(HistoryNoteDto.class);

        HistoryNote expectedNote = mock(HistoryNote.class);
        when(historyNoteDtoEntityMapper.daoToEntity(givenDto)).thenReturn(expectedNote);
        when(historyNoteDtoEntityMapper.entityToDto(expectedNote)).thenReturn(expectedDto);

        HistoryNoteDto actualDto = historyNoteService.addNote(givenDto);

        verify(historyNoteDtoEntityMapper).daoToEntity(givenDto);
        verify(historyNoteRepository).save(any());

        assertEquals(expectedDto, actualDto);
        assertNotSame(givenDto, actualDto);
    }
    @Test
    void addNote_whenCaptchaDtoGiven_thenCallHistoryNoteDtoFactory() {
        String givenLogin = "john";
        when(principal.getLogin()).thenReturn(givenLogin);
        historyNoteService.setPrincipal(principal);
        HistoryNoteDto expectedDto = mock(HistoryNoteDto.class);
        CaptchaDto givenDto = mock(CaptchaDto.class);
        ActionType givenType = ActionType.CAPTCHA_CREATE;

        when(historyNoteService.addNote(expectedDto)).thenReturn(expectedDto);

        HistoryNoteDto actualDto = historyNoteService.addNote(givenType, givenDto);

        verify(historyNoteDtoFactory).createNote(givenType, givenLogin, givenDto);
        verify(historyNoteService).addNote(expectedDto);
        assertEquals(expectedDto, actualDto);
    }
    @Test
    void addNote_whenCaptchaTypeDtoGiven_thenCallHistoryNoteDtoFactory() {
        String givenLogin = "john";
        when(principal.getLogin()).thenReturn(givenLogin);
        historyNoteService.setPrincipal(principal);
        HistoryNoteDto expectedDto = mock(HistoryNoteDto.class);
        CaptchaTypeDto givenDto = mock(CaptchaTypeDto.class);
        ActionType givenType = ActionType.CAPTCHA_CREATE;

        when(historyNoteService.addNote(expectedDto)).thenReturn(expectedDto);

        HistoryNoteDto actualDto = historyNoteService.addNote(givenType, givenDto);

        verify(historyNoteDtoFactory).createNote(givenType, givenLogin, givenDto);
        verify(historyNoteService).addNote(expectedDto);
        assertEquals(expectedDto, actualDto);
    }
}
