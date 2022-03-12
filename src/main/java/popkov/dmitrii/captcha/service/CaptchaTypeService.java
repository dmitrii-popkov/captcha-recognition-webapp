package popkov.dmitrii.captcha.service;

import java.util.List;
import popkov.dmitrii.captcha.dto.json.CaptchaTypeDto;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import popkov.dmitrii.captcha.mapper.CaptchaTypeDtoEntityMapper;
import popkov.dmitrii.captcha.repository.captcha.CaptchaTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CaptchaTypeService {

    private final CaptchaTypeDtoEntityMapper captchaTypeDtoEntityMapper;
    private final CaptchaTypeRepository captchaTypeRepository;

    public List<CaptchaTypeDto> getAllTags() {
        return captchaTypeDtoEntityMapper
                .entityToDto(captchaTypeRepository.findAll());
    }

    public CaptchaTypeDto addTag(String tag) {
        CaptchaType captchaType = captchaTypeDtoEntityMapper.stringToEntity(tag);
        captchaTypeRepository.save(captchaType);
        return captchaTypeDtoEntityMapper.entityToDto(captchaType);
    }
}
