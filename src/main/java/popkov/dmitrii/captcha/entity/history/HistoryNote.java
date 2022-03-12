package popkov.dmitrii.captcha.entity.history;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import popkov.dmitrii.captcha.entity.captcha.Captcha;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import popkov.dmitrii.captcha.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryNote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACTION_ID")
    private HistoryAction action;

    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "SERVICE_USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CAPTCHA_ID")
    private Captcha captcha;

    @ManyToOne
    @JoinColumn
    private CaptchaType captchaType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        HistoryNote that = (HistoryNote) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
