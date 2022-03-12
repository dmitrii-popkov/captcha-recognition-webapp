package popkov.dmitrii.captcha;

import java.util.ArrayList;
import java.util.List;

import popkov.dmitrii.captcha.dto.filter.CaptchaFilterForm;
import popkov.dmitrii.captcha.dto.json.CaptchaDto;
import popkov.dmitrii.captcha.entity.captcha.Captcha;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.mapper.CaptchaDtoEntityMapperImpl;
import popkov.dmitrii.captcha.repository.captcha.CaptchaRepository;
import popkov.dmitrii.captcha.repository.captcha.CaptchaTypeRepository;
import popkov.dmitrii.captcha.repository.captcha.PredictionResultRepository;
import popkov.dmitrii.captcha.service.CaptchaService;
import popkov.dmitrii.captcha.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptchaServiceTests {

    @InjectMocks
    private CaptchaService captchaService;
    @Spy
    private CaptchaRepository captchaRepository;
    @Spy
    private CaptchaDtoEntityMapperImpl captchaDtoEntityMapper;
    @Mock
    private PredictionResultRepository predictionResultRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private CaptchaTypeRepository captchaTypeRepository;
    @Mock
    private User principal;

    @Test
    void getCaptchasBy_whenFilterFormGiven_thenCallCaptchaRepository() {
        List<Captcha> givenCaptchas = List.of(new Captcha(), new Captcha());
        List<CaptchaDto> expectedDtos = List.of(new CaptchaDto(), new CaptchaDto());
        CaptchaFilterForm givenFilterForm = new CaptchaFilterForm();

        Specification<Captcha> expectedLambda = captchaRepository.appliesFilter(givenFilterForm);
        when(captchaRepository.appliesFilter(givenFilterForm)).thenReturn(expectedLambda);
        when(captchaRepository.findAll(expectedLambda)).thenReturn(givenCaptchas);

        List<CaptchaDto> actualDtos = captchaService.getCaptchasBy(givenFilterForm);

        ArgumentCaptor<List<Captcha>> captchaCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(captchaRepository).findAll(expectedLambda);
        verify(captchaDtoEntityMapper).entityToDto(captchaCaptor.capture());

        assertEquals(expectedDtos, actualDtos);
        assertEquals(captchaCaptor.getValue(), givenCaptchas);
    }

    @Test
    void editCaptcha_whenDtoGiven_thenSavePredictionResultAndCaptchaTypeAndCaptcha() {
        CaptchaDto captchaDto = new CaptchaDto();
        Captcha expectedCaptcha = mock(Captcha.class);

        when(captchaDtoEntityMapper.dtoToEntity(any())).thenReturn(expectedCaptcha);

        CaptchaDto actualDto = captchaService.editCaptcha(captchaDto);

        verify(captchaDtoEntityMapper).dtoToEntity(captchaDto);
        verify(captchaRepository).save(expectedCaptcha);
        verify(captchaTypeRepository).saveAll(expectedCaptcha.getCaptchaType());
        verify(predictionResultRepository).save(expectedCaptcha.getPredictionResult());

        assertEquals(captchaDto, actualDto);

    }

    @Test
    void createCaptcha_whenImageGiven_thenSaveImageAndCaptchaAndPredictionResult() {
        String givenFilename = "filename";
        String givenUsername = "john";
        CaptchaDto expectedDto = CaptchaDto.builder()
                .name(givenFilename)
                .author(givenUsername)
                .type(new ArrayList<>())
                .answer(givenFilename)
                .isIsRight(false)
                .imagePath(givenFilename).build();
        Captcha expectedCaptcha = mock(Captcha.class);
        MultipartFile givenFile = mock(MultipartFile.class);

        captchaService.setPrincipal(principal);
        when(imageService.saveFile(givenFile)).thenReturn(givenFilename);
        when(principal.getLogin()).thenReturn(givenUsername);
        when(captchaDtoEntityMapper.dtoToEntity(any())).thenReturn(expectedCaptcha);

        CaptchaDto actualDto = captchaService.createCaptcha(givenFile);

        verify(imageService).saveFile(givenFile);
        verify(predictionResultRepository).save(expectedCaptcha.getPredictionResult());
        verify(captchaRepository).save(expectedCaptcha);
        assertEquals(expectedDto, actualDto);
    }

}
