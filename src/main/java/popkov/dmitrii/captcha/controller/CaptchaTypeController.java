package popkov.dmitrii.captcha.controller;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import popkov.dmitrii.captcha.dto.json.CaptchaTypeDto;
import popkov.dmitrii.captcha.entity.history.ActionType;
import popkov.dmitrii.captcha.mapper.HistoryNoteDtoFactory;
import popkov.dmitrii.captcha.security.TokenProvider;
import popkov.dmitrii.captcha.service.CaptchaTypeService;
import popkov.dmitrii.captcha.service.HistoryNoteService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CaptchaTypeController {
    private final TokenProvider tokenProvider;
    private final HistoryNoteDtoFactory historyNoteDtoFactory;
    private final HistoryNoteService historyNoteService;
    private final CaptchaTypeService captchaTypeService;

    @GetMapping("/api/captchas/tags")
    public ResponseEntity<List<CaptchaTypeDto>> getAllTags() {
        return ResponseEntity.ok(captchaTypeService.getAllTags());
    }

    @PostMapping("/api/captchas/tags")
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<CaptchaTypeDto> addTag(@RequestBody CaptchaTypeDto captchaType) {
        CaptchaTypeDto captchaTypeDto = captchaTypeService.addTag(captchaType.getName());
        historyNoteService.addNote(ActionType.TAG_CREATE, captchaTypeDto);
        return ResponseEntity.ok(captchaTypeDto);
    }
}
