package popkov.dmitrii.captcha.controller;

import java.util.List;
import popkov.dmitrii.captcha.dto.json.HistoryNoteDto;
import popkov.dmitrii.captcha.service.HistoryNoteService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryNoteController {

    private final HistoryNoteService historyNoteService;

    @GetMapping("/api/history")
    public ResponseEntity<List<HistoryNoteDto>> getNotes() {
        return ResponseEntity.ok(historyNoteService.getNotes());
    }
}
