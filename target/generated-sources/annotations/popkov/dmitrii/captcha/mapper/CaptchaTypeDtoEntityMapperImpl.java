package popkov.dmitrii.captcha.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import popkov.dmitrii.captcha.dto.json.CaptchaTypeDto;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-12T21:51:30+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.9 (JetBrains s.r.o.)"
)
@Component
public class CaptchaTypeDtoEntityMapperImpl extends CaptchaTypeDtoEntityMapper {

    @Autowired
    private CaptchaTypeEntityFactory captchaTypeEntityFactory;

    @Override
    public CaptchaTypeDto entityToDto(CaptchaType entity) {
        if ( entity == null ) {
            return null;
        }

        CaptchaTypeDto captchaTypeDto = new CaptchaTypeDto();

        captchaTypeDto.setName( entity.getTypeName() );

        captchaTypeDto.setCount( captchaRepository.countByCaptchaType(entity) );

        return captchaTypeDto;
    }

    @Override
    public List<CaptchaTypeDto> entityToDto(List<CaptchaType> entity) {
        if ( entity == null ) {
            return null;
        }

        List<CaptchaTypeDto> list = new ArrayList<CaptchaTypeDto>( entity.size() );
        for ( CaptchaType captchaType : entity ) {
            list.add( entityToDto( captchaType ) );
        }

        return list;
    }

    @Override
    public CaptchaType stringToEntity(String tag) {
        if ( tag == null ) {
            return null;
        }

        CaptchaType captchaType = captchaTypeEntityFactory.lookup( tag );

        captchaType.setTypeName( tag );

        return captchaType;
    }
}
