package popkov.dmitrii.captcha.repository.captcha;

import popkov.dmitrii.captcha.entity.captcha.PredictionResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionResultRepository extends CrudRepository<PredictionResult, Long> {
    PredictionResult findByAnswerAndIsRight(String answer, boolean isRight);
}
