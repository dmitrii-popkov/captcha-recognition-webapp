package popkov.dmitrii.captcha.repository.captcha;

import java.util.List;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaTypeRepository extends CrudRepository<CaptchaType, Long> {

    List<CaptchaType> findAll();
    List<CaptchaType> findCaptchaTypesByTypeName(String name);
    CaptchaType findCaptchaTypeByTypeName(String name);
}
