package tn.cloudnine.queute.config.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.cloudnine.queute.model.user.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserToUserDetails implements UserDetails {
    private String email;
    private String password;
    private Long userId;
    private Long organizationId;
    private List<GrantedAuthority> authorities;

    public UserToUserDetails(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.userId = user.getUserId();
        if (user.getOrganization() != null && user.getOrganization().getOrganizationId() != null)
            this.organizationId = user.getOrganization().getOrganizationId();
        else
            this.organizationId = -1L;
        this.authorities = Arrays.stream(
                        user.getRole().toString().split(",")
                )
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {  // Spring Security uses "username" not "email"
        return email;
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