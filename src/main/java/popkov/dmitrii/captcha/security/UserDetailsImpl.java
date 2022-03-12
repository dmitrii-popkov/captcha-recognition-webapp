package popkov.dmitrii.captcha.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import popkov.dmitrii.captcha.entity.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = user.getUserRole().getRoleType().name();
        List<GrantedAuthority> grantedAuthorities = null;
        if (roleName.equals("USER")) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
            grantedAuthorities = Collections.singletonList(grantedAuthority);
        } else if (roleName.equals("ADMIN")) {
            GrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
            GrantedAuthority userAuthority = new SimpleGrantedAuthority("ROLE_USER");
            grantedAuthorities = Arrays.asList(adminAuthority, userAuthority);
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
