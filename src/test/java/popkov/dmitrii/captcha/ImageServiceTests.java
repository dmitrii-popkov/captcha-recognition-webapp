package popkov.dmitrii.captcha;

import java.io.IOException;
import popkov.dmitrii.captcha.exception.ApplicationException;
import popkov.dmitrii.captcha.service.FileWriter;
import popkov.dmitrii.captcha.service.ImageService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTests {
    @InjectMocks
    private ImageService imageService;

    @Mock
    private FileWriter fileWriter;

    @ParameterizedTest
    @SneakyThrows
    @ValueSource(strings = {"png", "jpg", "png"})
    void saveFile_whenImageGiven_thenPersistExtension(String expectedExtension) {

        MultipartFile givenFile = mock(MultipartFile.class);

        when(givenFile.getContentType()).thenReturn("image/" + expectedExtension);
        when(givenFile.getBytes()).thenReturn(new byte[0]);
        String actualFilename = imageService.saveFile(givenFile);
        String actualExtension = actualFilename.substring(actualFilename.indexOf(".") + 1);

        assertEquals(expectedExtension, actualExtension);


    }

    @Test
    void saveFile_whenFileNotSaved_thenThrowException() {
        assertThrows(ApplicationException.class, () -> {

            MultipartFile givenFile = mock(MultipartFile.class);
            when(givenFile.getContentType()).thenReturn("image/jpg");
            when(givenFile.getBytes()).thenReturn(new byte[0]);

            doThrow(IOException.class).when(fileWriter).write(any(), any());

            imageService.saveFile(givenFile);
        });
    }
}
