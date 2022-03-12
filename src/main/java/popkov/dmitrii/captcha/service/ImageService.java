package popkov.dmitrii.captcha.service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import popkov.dmitrii.captcha.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final String imageFolder;
    private final FileWriter fileWriter;
    @Autowired
    public ImageService(@Value("${image.folder}") String imageFolder, FileWriter fileWriter) {
        this.imageFolder = imageFolder;
        this.fileWriter = fileWriter;
    }

    public String saveFile(MultipartFile file) {
        try {
            String fileName = String.format("%s.%s", UUID.randomUUID(),
                    Objects.requireNonNull(file.getContentType()).replace("image/", ""));
            File convertFile = new File(String.format("%s%s", imageFolder, fileName));
            fileWriter.write(convertFile, file.getBytes());
            return fileName;
        } catch (IOException e) {
            throw new ApplicationException("Could not save image", e);
        }

    }
}
