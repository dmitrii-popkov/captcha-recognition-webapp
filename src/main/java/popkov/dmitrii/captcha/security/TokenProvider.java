package popkov.dmitrii.captcha.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import popkov.dmitrii.captcha.entity.user.User;
import popkov.dmitrii.captcha.exception.ApplicationException;
import popkov.dmitrii.captcha.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TokenProvider {
    private static final String SECRET =
            "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final String AUTHORITIES_KEY = "auth";

    private final UserRepository userRepository;
    private Key key;

    @PostConstruct
    public void setKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        key = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());
    }
    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.", e);
            throw new ApplicationException("Jwt token invalid", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.", e);
            throw new ApplicationException("Jwt token invalid", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.", e);
            throw new ApplicationException("Jwt token invalid", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.", e);
            throw new ApplicationException("Jwt token invalid", e);
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String authToken) {
        validateToken(authToken);
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(authToken)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = userRepository.findByLogin(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
    }

    public String getUsernameFromToken(String token) {
        validateToken(token);
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
