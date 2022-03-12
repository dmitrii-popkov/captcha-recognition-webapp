package popkov.dmitrii.captcha.config;

import java.io.File;
import java.util.Date;
import java.util.List;
import popkov.dmitrii.captcha.entity.captcha.Captcha;
import popkov.dmitrii.captcha.entity.captcha.CaptchaType;
import popkov.dmitrii.captcha.entity.captcha.PredictionResult;
import popkov.dmitrii.captcha.entity.history.ActionType;
import popkov.dmitrii.captcha.entity.history.HistoryAction;
import popkov.dmitrii.captcha.entity.history.HistoryNote;
import popkov.dmitrii.captcha.entity.user.RoleType;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.entity.user.UserRole;
import popkov.dmitrii.captcha.repository.captcha.CaptchaRepository;
import popkov.dmitrii.captcha.repository.captcha.CaptchaTypeRepository;
import popkov.dmitrii.captcha.repository.captcha.PredictionResultRepository;
import popkov.dmitrii.captcha.repository.history.HistoryActionRepository;
import popkov.dmitrii.captcha.repository.history.HistoryNoteRepository;
import popkov.dmitrii.captcha.repository.user.UserRepository;
import popkov.dmitrii.captcha.repository.user.UserRoleRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:dev.properties")
@PropertySource("classpath:app.properties")
public class DevelopmentConfig {

    @Bean
    @Autowired
    CommandLineRunner removeImages(@Value("${app.purge.images}") boolean removeImages,
                                   @Value("${image.folder}") String folder){
        return args -> {
            if(removeImages) {
                FileUtils.cleanDirectory(new File(folder));
            }
        };
    }
    @Bean
    @Autowired
    CommandLineRunner commandLineRunner(@Value("${app.insert.demo}") boolean insertDemo,
                                        CaptchaRepository captchaRepository,
                                        CaptchaTypeRepository captchaTypeRepository,
                                        PredictionResultRepository predictionResultRepository,
                                        HistoryNoteRepository historyNoteRepository,
                                        HistoryActionRepository historyActionRepository,
                                        UserRepository userRepository,
                                        UserRoleRepository userRoleRepository) {
        return args -> {
            if (insertDemo) {
                deleteData(captchaRepository, captchaTypeRepository,
                        predictionResultRepository, historyNoteRepository,
                        historyActionRepository, userRepository,
                        userRoleRepository);

                insertData(captchaRepository, captchaTypeRepository,
                        predictionResultRepository, historyNoteRepository,
                        historyActionRepository, userRepository,
                        userRoleRepository);
            }
        };
    }

    private void insertData(CaptchaRepository captchaRepository, CaptchaTypeRepository captchaTypeRepository, PredictionResultRepository predictionResultRepository, HistoryNoteRepository historyNoteRepository, HistoryActionRepository historyActionRepository, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        UserRole userRole = UserRole.builder().roleType(RoleType.USER).build();
        UserRole adminRole = UserRole.builder().roleType(RoleType.ADMIN).build();
        UserRole anonymousRole = UserRole.builder().roleType(RoleType.ANONYMOUS).build();
        userRoleRepository.saveAll(List.of(userRole, adminRole, anonymousRole));

        User user = User.builder().userRole(userRole).login("john").password("doe").picturePath("avatar.png").build();
        User admin = User.builder().userRole(adminRole).login("admin").password("admin").picturePath("admin.jpg").build();
        User anonymous = User.builder().userRole(anonymousRole).login("anonymous").build();
        userRepository.saveAll(List.of(user, admin, anonymous));

        CaptchaType captchasNet = CaptchaType.builder().typeName("captchas_net").build();
        CaptchaType recaptcha = CaptchaType.builder().typeName("recaptcha").build();
        CaptchaType wolframAlpha = CaptchaType.builder().typeName("wolfram_alpha").build();
        CaptchaType abc = CaptchaType.builder().typeName("abc").build();
        CaptchaType wikipedia = CaptchaType.builder().typeName("wikipedia").build();
        captchaTypeRepository.saveAll(List.of(captchasNet, recaptcha, wolframAlpha, abc, wikipedia));

        PredictionResult captchasNetResult = PredictionResult.builder().answer("captchas.net").isRight(true).build();
        PredictionResult someWordResult = PredictionResult.builder().answer("Some word").isRight(true).build();
        PredictionResult wvphnhResult = PredictionResult.builder().answer("wvphnh").isRight(true).build();
        PredictionResult tpkjxResult = PredictionResult.builder().answer("tpkjx").isRight(true).build();
        PredictionResult xtztsdResult = PredictionResult.builder().answer("xtztsd").isRight(true).build();
        predictionResultRepository.saveAll(List.of(captchasNetResult, someWordResult, wvphnhResult, tpkjxResult, xtztsdResult));

        Captcha captchasNetCaptcha = Captcha.builder().name("captchas.net.png")
                .imagePath("captchas.net.png").user(anonymous).captchaType(List.of(captchasNet))
                .predictionResult(captchasNetResult).build();
        Captcha someWordCapthca = Captcha.builder().name("Some word.png").imagePath("/")
                .user(anonymous).captchaType(List.of(recaptcha))
                .predictionResult(someWordResult).build();
        Captcha wvphnhCaptcha = Captcha.builder().name("wvphnh.png").imagePath("/")
                .user(anonymous).captchaType(List.of(captchasNet))
                .predictionResult(wvphnhResult).build();
        Captcha tpkjxCaptcha = Captcha.builder().name("tpkjx.png").imagePath("/")
                .user(anonymous).captchaType(List.of(wolframAlpha, abc))
                .predictionResult(tpkjxResult).build();
        Captcha xtztsdCaptcha = Captcha.builder().name("xtztsd.png").imagePath("/")
                .user(anonymous).captchaType(List.of(captchasNet))
                .predictionResult(xtztsdResult).build();
        captchaRepository.saveAll(List.of(captchasNetCaptcha, someWordCapthca,
                wvphnhCaptcha, tpkjxCaptcha, xtztsdCaptcha));

        HistoryAction action = HistoryAction.builder().actionType(ActionType.CAPTCHA_CREATE)
                .description("%s added %S").build();

        HistoryAction editAction = HistoryAction.builder().actionType(ActionType.CAPTCHA_EDIT)
                .description("%s edited %s").build();

        HistoryAction tagCreateAction = HistoryAction.builder().actionType(ActionType.TAG_CREATE)
                .description("%s created tag %s").build();

        historyActionRepository.saveAll(List.of(action, editAction, tagCreateAction));

        HistoryNote note = HistoryNote.builder().action(action)
                .captcha(xtztsdCaptcha)
                .user(anonymous)
                .createdAt(new Date(0)).build();

        historyNoteRepository.save(note);
    }

    private void deleteData(CaptchaRepository captchaRepository,
                            CaptchaTypeRepository captchaTypeRepository,
                            PredictionResultRepository predictionResultRepository,
                            HistoryNoteRepository historyNoteRepository,
                            HistoryActionRepository historyActionRepository,
                            UserRepository userRepository,
                            UserRoleRepository userRoleRepository) {
        historyNoteRepository.deleteAll();
        captchaRepository.deleteAll();
        captchaTypeRepository.deleteAll();
        predictionResultRepository.deleteAll();
        historyActionRepository.deleteAll();
        userRepository.deleteAll();
        userRoleRepository.deleteAll();
    }
}
