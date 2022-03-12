package popkov.dmitrii.captcha.mapper;

import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import popkov.dmitrii.captcha.repository.captcha.CaptchaTypeRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class CaptchaTypeEntityFactory {

    private final CaptchaTypeRepository captchaTypeRepository;

    @ObjectFactory
    public CaptchaType lookup(String tag) {
        CaptchaType entity = captchaTypeRepository.findCaptchaTypeByTypeName(tag);
        if (entity == null) {
            entity = CaptchaType.builder().build();
        }
        return entity;
    }
}
