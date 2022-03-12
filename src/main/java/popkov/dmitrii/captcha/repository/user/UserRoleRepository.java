package popkov.dmitrii.captcha.repository.user;

import popkov.dmitrii.captcha.entity.user.RoleType;
import popkov.dmitrii.captcha.entity.user.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    UserRole findByRoleType(RoleType roleType);
}
