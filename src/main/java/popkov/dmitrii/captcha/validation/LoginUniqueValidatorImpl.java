package popkov.dmitrii.captcha.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import popkov.dmitrii.captcha.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class LoginUniqueValidatorImpl implements ConstraintValidator<LoginUniqueValidator, String> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByLogin(s);
    }
}
