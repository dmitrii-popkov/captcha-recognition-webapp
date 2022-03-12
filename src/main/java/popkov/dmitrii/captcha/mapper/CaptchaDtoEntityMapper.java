package popkov.dmitrii.captcha.mapper;

import java.util.List;
import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.entity.captcha.Captcha;
import popkov.dmitrii.captcha.entity.captcha.PredictionResult;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.repository.captcha.CaptchaRepository;
import popkov.dmitrii.captcha.repository.captcha.CaptchaTypeRepository;
import popkov.dmitrii.captcha.repository.captcha.PredictionResultRepository;
import popkov.dmitrii.captcha.repository.user.UserRepository;
import lombok.Setter;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Setter(onMethod = @__(@Autowired))
@Mapper(componentModel = "spring",
        uses = {CaptchaEntityFactory.class, CaptchaTypeDtoEntityMapper.class},
        builder = @Builder(disableBuilder = true))
public abstract class CaptchaDtoEntityMapper {

    protected UserRepository userRepository;
    protected CaptchaTypeRepository captchaTypeRepository;
    protected CaptchaRepository captchaRepository;
    protected PredictionResultRepository predictionResultRepository;

    @Mapping(target = "author", source = "entity.user.login")
    @Mapping(target = "answer", source = "entity.predictionResult.answer")
    @Mapping(target = "isRight", source = "entity.predictionResult.isRight")
    @Mapping(target = "type", source = "entity.captchaType")
    @Mapping(target = "imagePath", source = "entity.imagePath")
    public abstract CaptchaDto entityToDto(Captcha entity);

    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "captchaType", source = "dto.type")
    @Mapping(target = "predictionResult", expression = "java(mapToPredictionResult(dto.getAnswer(), dto.isIsRight()))")
    @Mapping(target = "imagePath", source = "dto.imagePath")
    @Mapping(target = "user", source = "dto.author")
    public abstract Captcha dtoToEntity(CaptchaDto dto);

    public abstract List<CaptchaDto> entityToDto(List<Captcha> entity);

    protected User mapStringToUser(String login) {
        return userRepository.findByLogin(login);
    }

    protected PredictionResult mapToPredictionResult(String answer, boolean isItRight) {
        PredictionResult entity = predictionResultRepository.findByAnswerAndIsRight(answer, isItRight);
        if (entity == null) {
            entity = PredictionResult.builder().answer(answer).isRight(isItRight).build();
        }
        return entity;
    }

}
