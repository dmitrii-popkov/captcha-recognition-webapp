package popkov.dmitrii.captcha.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.entity.captcha.Captcha;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import popkov.dmitrii.captcha.entity.captcha.PredictionResult;
import popkov.dmitrii.captcha.entity.user.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-12T21:51:30+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.9 (JetBrains s.r.o.)"
)
@Component
public class CaptchaDtoEntityMapperImpl extends CaptchaDtoEntityMapper {

    @Autowired
    private CaptchaEntityFactory captchaEntityFactory;
    @Autowired
    private CaptchaTypeDtoEntityMapper captchaTypeDtoEntityMapper;

    @Override
    public CaptchaDto entityToDto(Captcha entity) {
        if ( entity == null ) {
            return null;
        }

        CaptchaDto captchaDto = new CaptchaDto();

        captchaDto.setAuthor( entityUserLogin( entity ) );
        captchaDto.setAnswer( entityPredictionResultAnswer( entity ) );
        Boolean isRight = entityPredictionResultIsRight( entity );
        if ( isRight != null ) {
            captchaDto.setIsRight( isRight );
        }
        captchaDto.setType( captchaTypeListToStringList( entity.getCaptchaType() ) );
        captchaDto.setImagePath( entity.getImagePath() );
        captchaDto.setName( entity.getName() );

        return captchaDto;
    }

    @Override
    public Captcha dtoToEntity(CaptchaDto dto) {
        if ( dto == null ) {
            return null;
        }

        Captcha captcha = captchaEntityFactory.lookup( dto );

        captcha.setName( dto.getName() );
        captcha.setCaptchaType( stringListToCaptchaTypeList( dto.getType() ) );
        captcha.setImagePath( dto.getImagePath() );
        captcha.setUser( mapStringToUser( dto.getAuthor() ) );

        captcha.setPredictionResult( mapToPredictionResult(dto.getAnswer(), dto.isIsRight()) );

        return captcha;
    }

    @Override
    public List<CaptchaDto> entityToDto(List<Captcha> entity) {
        if ( entity == null ) {
            return null;
        }

        List<CaptchaDto> list = new ArrayList<CaptchaDto>( entity.size() );
        for ( Captcha captcha : entity ) {
            list.add( entityToDto( captcha ) );
        }

        return list;
    }

    private String entityUserLogin(Captcha captcha) {
        if ( captcha == null ) {
            return null;
        }
        User user = captcha.getUser();
        if ( user == null ) {
            return null;
        }
        String login = user.getLogin();
        if ( login == null ) {
            return null;
        }
        return login;
    }

    private String entityPredictionResultAnswer(Captcha captcha) {
        if ( captcha == null ) {
            return null;
        }
        PredictionResult predictionResult = captcha.getPredictionResult();
        if ( predictionResult == null ) {
            return null;
        }
        String answer = predictionResult.getAnswer();
        if ( answer == null ) {
            return null;
        }
        return answer;
    }

    private Boolean entityPredictionResultIsRight(Captcha captcha) {
        if ( captcha == null ) {
            return null;
        }
        PredictionResult predictionResult = captcha.getPredictionResult();
        if ( predictionResult == null ) {
            return null;
        }
        Boolean isRight = predictionResult.getIsRight();
        if ( isRight == null ) {
            return null;
        }
        return isRight;
    }

    protected List<String> captchaTypeListToStringList(List<CaptchaType> list) {
        if ( list == null ) {
            return null;
        }

        List<String> list1 = new ArrayList<String>( list.size() );
        for ( CaptchaType captchaType : list ) {
            list1.add( captchaTypeDtoEntityMapper.mapCaptchaTypeToString( captchaType ) );
        }

        return list1;
    }

    protected List<CaptchaType> stringListToCaptchaTypeList(List<String> list) {
        if ( list == null ) {
            return null;
        }

        List<CaptchaType> list1 = new ArrayList<CaptchaType>( list.size() );
        for ( String string : list ) {
            list1.add( captchaTypeDtoEntityMapper.stringToEntity( string ) );
        }

        return list1;
    }
}
