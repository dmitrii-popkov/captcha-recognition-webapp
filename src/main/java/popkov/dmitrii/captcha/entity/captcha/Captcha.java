package popkov.dmitrii.captcha.entity.captcha;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import popkov.dmitrii.captcha.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Builder
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "CAPTCHA_CAPTCHA_TYPE",
            joinColumns = @JoinColumn(name = "CAPTCHA_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name="TYPE_ID", referencedColumnName = "ID")
    )

    private List<CaptchaType> captchaType;

    @ManyToOne
    @JoinColumn(name = "SERVICE_USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "RESULT_ID")
    private PredictionResult predictionResult;

    private String imagePath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Captcha captcha = (Captcha) o;
        return id != null && Objects.equals(id, captcha.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
