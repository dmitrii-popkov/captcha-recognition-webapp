package popkov.dmitrii.captcha.repository.user;

import popkov.dmitrii.captcha.entity.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);
    boolean existsByLogin(String login);
}
