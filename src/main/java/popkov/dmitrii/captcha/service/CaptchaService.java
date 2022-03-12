package popkov.dmitrii.captcha.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import popkov.dmitrii.captcha.dto.filter.CaptchaFilterForm;
import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.entity.captcha.Captcha;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.mapper.CaptchaDtoEntityMapper;
import popkov.dmitrii.captcha.repository.captcha.CaptchaRepository;
import popkov.dmitrii.captcha.repository.captcha.CaptchaTypeRepository;
import popkov.dmitrii.captcha.repository.captcha.PredictionResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CaptchaService {

    private final CaptchaRepository captchaRepository;
    private final CaptchaDtoEntityMapper captchaDtoEntityMapper;
    private final PredictionResultRepository predictionResultRepository;
    private final ImageService imageService;
    private final CaptchaTypeRepository captchaTypeRepository;

    @Setter(onMethod = @__(@Autowired))
    private User principal;

    public List<CaptchaDto> getCaptchasBy(CaptchaFilterForm filterForm) {
        return captchaDtoEntityMapper.entityToDto(
                captchaRepository.findAll(captchaRepository.appliesFilter(filterForm)));
    }

    public CaptchaDto editCaptcha(CaptchaDto captchaDto) {
        Captcha captcha = captchaDtoEntityMapper.dtoToEntity(captchaDto);
        predictionResultRepository.save(captcha.getPredictionResult());
        captchaTypeRepository.saveAll(captcha.getCaptchaType());
        captchaRepository.save(captcha);
        return captchaDto;
    }

    @Transactional
    // TODO: 3/10/22 Consider making rollbackEvent
    public CaptchaDto createCaptcha(MultipartFile img) {
        String filename = imageService.saveFile(img);
        CaptchaDto captchaDto = CaptchaDto.builder()
                .name(filename)
                .author(principal.getLogin())
                .type(new ArrayList<>())
                .answer(filename)
                .isIsRight(false)
                .imagePath(filename)
                .build();

        Captcha captcha = captchaDtoEntityMapper.dtoToEntity(captchaDto);

        predictionResultRepository.save(captcha.getPredictionResult());
        captchaRepository.save(captcha);
        return captchaDto;
    }
}
