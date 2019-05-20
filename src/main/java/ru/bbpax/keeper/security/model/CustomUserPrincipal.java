package ru.bbpax.keeper.security.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomUserPrincipal implements UserDetails {
    private final User user;

    public CustomUserPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Map<String, List<Privilege>> getPrivileges() {
        return user.getPrivileges();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public boolean hasPrivilegeFor(String targetId, String accessLevel) {
        final List<Privilege> privileges = user.getPrivileges().get(targetId);

        return privileges != null && privileges.stream()
                .anyMatch(privilege -> accessLevel.equals(privilege.getAccessLevel()));
    }

    public void addPrivilege(String targetId, String accessLevel) {
        final List<Privilege> privileges = user.getPrivileges().get(targetId);
        if (privileges == null) {
            user.getPrivileges()
                    .put(targetId, Collections.singletonList(new Privilege(accessLevel)));
        } else if (privileges.stream()
                .noneMatch(privilege -> accessLevel.equals(privilege.getAccessLevel()))) {
            privileges.add(new Privilege(accessLevel));
        }
        log.info("user updated in memory: {}", user);

    }

    @Override
    public String toString() {
        return "CustomUserPrincipal{" +
                "user=" + user +
                '}';
    }
}
