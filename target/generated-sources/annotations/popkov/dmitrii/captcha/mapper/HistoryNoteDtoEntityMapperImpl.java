package popkov.dmitrii.captcha.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import popkov.dmitrii.captcha.dto.json.HistoryNoteDto;
import popkov.dmitrii.captcha.dto.json.HistoryNoteDto.HistoryNoteDtoBuilder;
import popkov.dmitrii.captcha.entity.history.ActionType;
import popkov.dmitrii.captcha.entity.history.HistoryAction;
import popkov.dmitrii.captcha.entity.history.HistoryNote;
import popkov.dmitrii.captcha.entity.history.HistoryNote.HistoryNoteBuilder;
import popkov.dmitrii.captcha.entity.user.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-12T21:51:30+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.9 (JetBrains s.r.o.)"
)
@Component
public class HistoryNoteDtoEntityMapperImpl extends HistoryNoteDtoEntityMapper {

    @Autowired
    private CaptchaDtoEntityMapper captchaDtoEntityMapper;
    @Autowired
    private CaptchaTypeDtoEntityMapper captchaTypeDtoEntityMapper;

    @Override
    public HistoryNoteDto entityToDto(HistoryNote entity) {
        if ( entity == null ) {
            return null;
        }

        HistoryNoteDtoBuilder historyNoteDto = HistoryNoteDto.builder();

        ActionType actionType = entityActionActionType( entity );
        if ( actionType != null ) {
            historyNoteDto.action( actionType.name() );
        }
        historyNoteDto.user( entityUserLogin( entity ) );
        historyNoteDto.stamp( entity.getCreatedAt() );
        historyNoteDto.captcha( captchaDtoEntityMapper.entityToDto( entity.getCaptcha() ) );
        historyNoteDto.tag( captchaTypeDtoEntityMapper.mapCaptchaTypeToString( entity.getCaptchaType() ) );
        historyNoteDto.description( mapDescriptionFromFormatPattern( entity ) );

        return historyNoteDto.build();
    }

    @Override
    public List<HistoryNoteDto> entityToDto(List<HistoryNote> entity) {
        if ( entity == null ) {
            return null;
        }

        List<HistoryNoteDto> list = new ArrayList<HistoryNoteDto>( entity.size() );
        for ( HistoryNote historyNote : entity ) {
            list.add( entityToDto( historyNote ) );
        }

        return list;
    }

    @Override
    public HistoryNote daoToEntity(HistoryNoteDto dto) {
        if ( dto == null ) {
            return null;
        }

        HistoryNoteBuilder historyNote = HistoryNote.builder();

        historyNote.action( mapToActionType( mapStringToType( dto.getAction() ) ) );
        historyNote.user( captchaDtoEntityMapper.mapStringToUser( dto.getUser() ) );
        historyNote.createdAt( dto.getStamp() );
        historyNote.captcha( captchaDtoEntityMapper.dtoToEntity( dto.getCaptcha() ) );
        historyNote.captchaType( captchaTypeDtoEntityMapper.stringToEntity( dto.getTag() ) );

        return historyNote.build();
    }

    private ActionType entityActionActionType(HistoryNote historyNote) {
        if ( historyNote == null ) {
            return null;
        }
        HistoryAction action = historyNote.getAction();
        if ( action == null ) {
            return null;
        }
        ActionType actionType = action.getActionType();
        if ( actionType == null ) {
            return null;
        }
        return actionType;
    }

    private String entityUserLogin(HistoryNote historyNote) {
        if ( historyNote == null ) {
            return null;
        }
        User user = historyNote.getUser();
        if ( user == null ) {
            return null;
        }
        String login = user.getLogin();
        if ( login == null ) {
            return null;
        }
        return login;
    }
}
