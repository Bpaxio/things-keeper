package ru.bbpax.keeper.security.operations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import ru.bbpax.keeper.security.model.CustomUserPrincipal;

import static ru.bbpax.keeper.security.model.AccessLevels.DELETE;
import static ru.bbpax.keeper.security.model.AccessLevels.OWN;
import static ru.bbpax.keeper.security.model.AccessLevels.READ;
import static ru.bbpax.keeper.security.model.AccessLevels.SHARE;
import static ru.bbpax.keeper.security.model.AccessLevels.WRITE;

@Slf4j
public class NotesMethodSecurityOperations
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private Object target;


    public NotesMethodSecurityOperations(Authentication authentication) {
        super(authentication);
    }

    public boolean hasReadPrivilege(String targetId) {
        return hasPrivilege(targetId, READ);
    }

    public boolean hasWritePrivilege(String targetId) {
        return hasPrivilege(targetId, WRITE);
    }

    public boolean hasDeletePrivilege(String targetId) {
        return hasPrivilege(targetId, DELETE);
    }

    public boolean hasSharePrivilege(String targetId) {
        return hasPrivilege(targetId, SHARE);
    }

    public boolean hasPrivilege(String targetId, String accessLevel) {
        log.info("verify if user has access {} for {}", accessLevel, targetId);
        if (!(this.getPrincipal() instanceof CustomUserPrincipal)) return false;
        if (isNoteOwner(targetId)) return true;
        CustomUserPrincipal principal = (CustomUserPrincipal) this.getPrincipal();
        return principal.hasPrivilegeFor(targetId, accessLevel);
    }

    public boolean isNoteOwner(String targetId) {
        log.info("verify if user is owner of note[id={}]", targetId);
        if (!(this.getPrincipal() instanceof CustomUserPrincipal)) return false;
        CustomUserPrincipal principal = (CustomUserPrincipal) this.getPrincipal();
        return principal.hasPrivilegeFor(targetId, OWN);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    /**
     * Sets the "this" property for use in expressions. Typically this will be the "this"
     * property of the {@code JoinPoint} representing the method invocation which is being
     * protected.
     *
     * @param target the target object on which the method in is being invoked.
     */
    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return target;
    }
}
