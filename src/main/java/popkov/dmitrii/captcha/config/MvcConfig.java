package popkov.dmitrii.captcha.config;

import popkov.dmitrii.captcha.convert.CaptchaFilterByNameConverter;
import popkov.dmitrii.captcha.entity.user.User;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebMvc
@PropertySource("classpath:app.properties")
public class MvcConfig implements WebMvcConfigurer {

    @Value("${image.folder}")
    private String imagePath;
    @Value("${avatar.folder}")
    private String avatarsPath;
    @Setter(onMethod = @__(@Autowired))
    private CaptchaFilterByNameConverter captchaFilterByNameConverter;
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("static/index.html");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(captchaFilterByNameConverter);
    }

    @Bean
    public ViewResolver viewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(InternalResourceView.class);
        return viewResolver;
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
    @Bean
    @RequestScope
    public User getPrincipal() {
        User result = User.builder().login("anonymous").build();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            result = (User) principal;
        }
        return result;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/static/images/captchas/" + "*")
                .addResourceLocations("file:///." + imagePath);
        registry.addResourceHandler("/static/images/avatars/*")
                .addResourceLocations("file:///." + avatarsPath);
    }

}
