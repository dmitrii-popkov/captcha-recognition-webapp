package popkov.dmitrii.captcha.mapper;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import popkov.dmitrii.captcha.dto.json.HistoryNoteDto;
import popkov.dmitrii.captcha.entity.history.ActionType;
import popkov.dmitrii.captcha.entity.history.HistoryAction;
import popkov.dmitrii.captcha.entity.history.HistoryNote;
import popkov.dmitrii.captcha.repository.history.HistoryActionRepository;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        uses = {CaptchaDtoEntityMapper.class, CaptchaTypeDtoEntityMapper.class})
public abstract class HistoryNoteDtoEntityMapper {

    @Setter(onMethod = @__(@Autowired))
    protected HistoryActionRepository historyActionRepository;

    protected Map<ActionType, Function<HistoryNote, Object[]>>
            actionTypeToParametersMap;

    @PostConstruct
    public void init() {
        actionTypeToParametersMap = Map.of(
                ActionType.CAPTCHA_CREATE,
                note -> new Object[] {note.getUser().getLogin(),
                        note.getCaptcha().getName()},
                ActionType.CAPTCHA_EDIT,
                note -> new Object[] {note.getUser().getLogin(),
                        note.getCaptcha().getName()},
                ActionType.TAG_CREATE,
                note -> new Object[] {note.getUser().getLogin(),
                        note.getCaptchaType().getTypeName()}
        );
    }
    @Mapping(target = "action", source = "entity.action.actionType")
    @Mapping(target = "user", source = "entity.user.login")
    @Mapping(target = "stamp", source = "entity.createdAt")
    @Mapping(target = "captcha", source = "entity.captcha")
    @Mapping(target = "tag", source = "entity.captchaType")
    @Mapping(target = "description", source = "entity")
    public abstract HistoryNoteDto entityToDto(HistoryNote entity);

    public abstract List<HistoryNoteDto> entityToDto(List<HistoryNote> entity);

    @Mapping(target = "action", source = "dto.action")
    @Mapping(target = "user", source = "dto.user")
    @Mapping(target = "createdAt", source = "dto.stamp")
    @Mapping(target = "captcha", source= "dto.captcha")
    @Mapping(target = "captchaType", source = "dto.tag")
    public abstract HistoryNote daoToEntity(HistoryNoteDto dto);

    protected HistoryAction mapToActionType(ActionType action) {
        return historyActionRepository.findByActionType(action);
    }
    protected ActionType mapStringToType(String source) {
        return ActionType.valueOf(source.toUpperCase(Locale.ROOT));
    }

    protected String mapDescriptionFromFormatPattern(HistoryNote entity) {

        return String.format(entity.getAction().getDescription(),
                actionTypeToParametersMap.get(entity.getAction().getActionType())
                        .apply(entity));
    }
}
