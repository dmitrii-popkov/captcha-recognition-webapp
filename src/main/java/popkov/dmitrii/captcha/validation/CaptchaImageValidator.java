package popkov.dmitrii.captcha.validation;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CaptchaImageValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        MultipartFile img = (MultipartFile) target;
        if (img.isEmpty()) {
            errors.rejectValue("img", "img.empty");
        }
        String contentType = img.getContentType();
        if (contentType == null || !contentType.matches("image/(png|jpg|jpeg)")) {
            errors.rejectValue("contentType", "content.type.invalid");
        }
    }
}
