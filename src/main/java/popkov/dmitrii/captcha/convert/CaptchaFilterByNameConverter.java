package popkov.dmitrii.captcha.convert;

import java.util.Locale;
import popkov.dmitrii.captcha.dto.filter.CaptchaFilter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CaptchaFilterByNameConverter implements Converter<String, CaptchaFilter> {
    @Override
    public CaptchaFilter convert(@NonNull String source) {
        return CaptchaFilter.valueOf(source.toUpperCase(Locale.ROOT));
    }
}
