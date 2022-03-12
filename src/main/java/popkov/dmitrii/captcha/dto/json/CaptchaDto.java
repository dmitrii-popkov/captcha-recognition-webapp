package popkov.dmitrii.captcha.dto.json;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaDto {
    @Size(min = 5, max = 300)
    private String name;
    @Size(min = 5, max = 300)
    private String author;
    @NotNull
    private List<String> type;
    private String answer;
    private boolean isIsRight;
    private String imagePath;
}
