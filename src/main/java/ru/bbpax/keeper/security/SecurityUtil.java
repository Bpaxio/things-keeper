package ru.bbpax.keeper.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.bbpax.keeper.security.model.CustomUserPrincipal;
import ru.bbpax.keeper.security.model.User;

public class SecurityUtil {
    public static User getWithPrivilege(String targetId, String accessLevel) {
        addPrivilege(targetId, accessLevel);
        return getUser();
    }

    public static void addPrivilege(String targetId, String accessLevel) {
        CustomUserPrincipal principal = (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        principal.addPrivilege(targetId, accessLevel);
    }

    public static User getUser() {
        return ((CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }
}
