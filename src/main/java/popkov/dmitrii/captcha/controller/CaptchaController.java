package popkov.dmitrii.captcha.controller;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import popkov.dmitrii.captcha.dto.filter.CaptchaFilterForm;
import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.entity.history.ActionType;
import popkov.dmitrii.captcha.service.CaptchaService;
import popkov.dmitrii.captcha.service.HistoryNoteService;
import popkov.dmitrii.captcha.validation.CaptchaImageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
@Slf4j
public class CaptchaController {

    private final HistoryNoteService historyNoteService;
    private final CaptchaService captchaService;
    private final CaptchaImageValidator imageValidator;

    @InitBinder("MultipartFile")
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(imageValidator);
    }
    @GetMapping(value = "/api/captchas", produces = "application/json")
    public ResponseEntity<List<CaptchaDto>> getCaptchas(CaptchaFilterForm filterForm) {
        return ResponseEntity.ok(captchaService.getCaptchasBy(filterForm));
    }

    @PostMapping("/api/captchas")
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<CaptchaDto> createCaptcha(
            @Valid @RequestPart("picture") MultipartFile img) throws BindException {
        BindingResult errors = new BeanPropertyBindingResult(img, "img");
        imageValidator.validate(img, errors);
        if (errors.hasErrors()) {
            throw new BindException(errors);
        }
        CaptchaDto captcha = captchaService.createCaptcha(img);
        historyNoteService.addNote(ActionType.CAPTCHA_CREATE, captcha);
        return ResponseEntity.ok(captcha);
    }
    @PutMapping("/api/captchas/{captchaName}")
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<CaptchaDto> updateCaptcha(@Valid @RequestBody CaptchaDto captchaDto) {
        CaptchaDto captcha = captchaService.editCaptcha(captchaDto);
        historyNoteService.addNote(ActionType.CAPTCHA_EDIT, captcha);
        return ResponseEntity.ok(captcha);
    }

}
