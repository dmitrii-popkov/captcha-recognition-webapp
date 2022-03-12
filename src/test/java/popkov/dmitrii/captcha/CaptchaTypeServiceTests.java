package popkov.dmitrii.captcha;

import java.util.List;
import popkov.dmitrii.captcha.dto.json.CaptchaTypeDto;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import popkov.dmitrii.captcha.mapper.CaptchaTypeDtoEntityMapperImpl;
import popkov.dmitrii.captcha.repository.captcha.CaptchaTypeRepository;
import popkov.dmitrii.captcha.service.CaptchaTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptchaTypeServiceTests {

    @InjectMocks
    private CaptchaTypeService captchaTypeService;

    @Mock
    private CaptchaTypeDtoEntityMapperImpl captchaTypeDtoEntityMapper;
    @Mock
    private CaptchaTypeRepository captchaTypeRepository;

    @Test
    void getAllTags_whenCalled_thenCallCaptchaTypeRepository() {
        CaptchaTypeDto dto = new CaptchaTypeDto();
        dto.setName("type");
        dto.setCount(1);
        CaptchaTypeDto otherDto = new CaptchaTypeDto();
        otherDto.setName("otherType");
        otherDto.setCount(2);
        CaptchaType expectedType = CaptchaType.builder().typeName("type").build();
        CaptchaType otherExpectedType = CaptchaType.builder().typeName("otherType").build();
        List<CaptchaTypeDto> expectedDtos = List.of(dto, otherDto);
        List<CaptchaType> givenTypes = List.of(expectedType, otherExpectedType);

        when(captchaTypeRepository.findAll()).thenReturn(givenTypes);
        when(captchaTypeDtoEntityMapper.entityToDto(givenTypes)).thenReturn(expectedDtos);

        List<CaptchaTypeDto> actualDtos = captchaTypeService.getAllTags();

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    void addTag_whenTagGiven_thenSaveTagAndReturnDto() {
        String givenTag = "tag";
        CaptchaType expectedType = CaptchaType.builder().typeName(givenTag).build();
        CaptchaTypeDto expectedDto = new CaptchaTypeDto();
        expectedDto.setCount(1);
        expectedDto.setName(givenTag);

        when(captchaTypeDtoEntityMapper.stringToEntity(givenTag)).thenReturn(expectedType);
        when(captchaTypeDtoEntityMapper.entityToDto(expectedType)).thenReturn(expectedDto);

        CaptchaTypeDto actualDto = captchaTypeService.addTag(givenTag);

        verify(captchaTypeRepository).save(expectedType);
        assertEquals(expectedDto, actualDto);
    }
}
