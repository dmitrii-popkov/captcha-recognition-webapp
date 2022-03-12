package popkov.dmitrii.captcha.mapper;

import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.entity.captcha.Captcha;
import popkov.dmitrii.captcha.repository.captcha.CaptchaRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class CaptchaEntityFactory {

    private final CaptchaRepository captchaRepository;

    @ObjectFactory
    public Captcha lookup(CaptchaDto captchaDto) {
        Captcha entity = captchaRepository
                .findByName(captchaDto.getName());
        if (entity == null) {
            entity = Captcha.builder().build();
        }
        return entity;
    }
}
