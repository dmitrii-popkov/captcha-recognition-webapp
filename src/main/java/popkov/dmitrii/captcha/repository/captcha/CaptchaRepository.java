package popkov.dmitrii.captcha.repository.captcha;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import popkov.dmitrii.captcha.dto.filter.CaptchaFilter;
import popkov.dmitrii.captcha.dto.filter.CaptchaFilterForm;
import popkov.dmitrii.captcha.entity.captcha.Captcha;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import popkov.dmitrii.captcha.entity.user.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long>, JpaSpecificationExecutor<Captcha> {
    @NonNull
    List<Captcha> findAll();
    Iterable<Captcha> findByUser(User user);
    int countByCaptchaType(CaptchaType captchaType);

    default Specification<Captcha> appliesFilter(CaptchaFilterForm filterForm) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            cq.distinct(true);

            if (filterForm.getName() != null && !filterForm.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), String.format("%%%s%%", filterForm.getName())));
            }

            if (filterForm.getTag() != null) {
                Join<Captcha, CaptchaType> types = root.join("captchaType");
                predicates.add(cb.equal(types.get("typeName"), filterForm.getTag()));
            }

            if (filterForm.getUser() != null) {
                predicates.add(cb.equal(root.get("user").get("login"), filterForm.getUser()));
            }

            CaptchaFilter filter = filterForm.getFilter();
            if (filter == null) {
                filter = CaptchaFilter.ALL;
            }
            boolean checkIsRight = filter.isCheckIsRight();
            boolean checkAnswer = filter.isCheckAnswer();
            boolean isRight = filter.isRight();

            if (checkIsRight) {
                predicates.add(cb.equal(root.get("predictionResult").get("isRight"), isRight));
            }
            if (checkAnswer) {
                predicates.add(cb.isNull(root.get("predictionResult").get("answer")));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
    Captcha findByName(String name);

}
