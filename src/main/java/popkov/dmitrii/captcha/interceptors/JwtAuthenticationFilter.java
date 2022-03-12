package popkov.dmitrii.captcha.interceptors;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import popkov.dmitrii.captcha.security.TokenProvider;
import lombok.Setter;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
@Setter(onMethod = @__(@Autowired))
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private UserDetailsService userDetailsService;
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            Optional<Cookie> token = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("token")).findAny();
            if (token.isPresent()) {
                String authToken = token.get().getValue();
                    String login = parseLoginFromToken(authToken);
                    if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        MDC.put("login", login);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
                        if (tokenProvider.validateToken(authToken)) {
                            UsernamePasswordAuthenticationToken authentication = tokenProvider
                                    .getAuthentication(authToken);
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
            }
        }
        chain.doFilter(req, res);
    }

    private String parseLoginFromToken(String token) {
        return tokenProvider.getUsernameFromToken(token);
    }
}
