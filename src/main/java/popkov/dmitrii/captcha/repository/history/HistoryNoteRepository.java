package popkov.dmitrii.captcha.repository.history;

import java.util.List;
import popkov.dmitrii.captcha.entity.history.HistoryNote;
import org.springframework.data.repository.CrudRepository;

public interface HistoryNoteRepository extends CrudRepository<HistoryNote, Long> {

    List<HistoryNote> getByUserLogin(String login);
}
