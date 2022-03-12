package popkov.dmitrii.captcha.repository.history;

import popkov.dmitrii.captcha.entity.history.ActionType;
import popkov.dmitrii.captcha.entity.history.HistoryAction;
import org.springframework.data.repository.CrudRepository;

public interface HistoryActionRepository extends CrudRepository<HistoryAction, Long> {
    HistoryAction findByActionType(ActionType actionType);
}
