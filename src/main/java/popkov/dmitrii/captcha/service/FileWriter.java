package popkov.dmitrii.captcha.service;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class FileWriter {

    public void write(File file, byte[] bytes) throws IOException {
        FileUtils.writeByteArrayToFile(file, bytes);
    }
}
