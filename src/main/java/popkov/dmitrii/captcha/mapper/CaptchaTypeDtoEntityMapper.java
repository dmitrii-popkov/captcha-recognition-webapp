package popkov.dmitrii.captcha.mapper;

import java.util.List;
import popkov.dmitrii.captcha.dto.json.CaptchaTypeDto;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import popkov.dmitrii.captcha.repository.captcha.CaptchaRepository;
import lombok.Setter;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        uses = {CaptchaTypeEntityFactory.class},
        builder = @Builder(disableBuilder = true))
@Setter(onMethod = @__(@Autowired))
public abstract class CaptchaTypeDtoEntityMapper {

    protected CaptchaRepository captchaRepository;

    @Mapping(target = "name", source = "entity.typeName")
    @Mapping(target = "count", expression = "java(captchaRepository.countByCaptchaType(entity))")
    public abstract CaptchaTypeDto entityToDto(CaptchaType entity);

    public abstract List<CaptchaTypeDto> entityToDto(List<CaptchaType> entity);


    @Mapping(target = "typeName", source = "tag")
    public abstract CaptchaType stringToEntity(String tag);

    protected String mapCaptchaTypeToString(CaptchaType source) {
        String result = null;
        if (source != null) {
            result = source.getTypeName();
        }
        return result;
    }
}
