package ru.bbpax.keeper.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import ru.bbpax.keeper.security.model.CustomUserPrincipal;
import ru.bbpax.keeper.security.model.Privilege;
import ru.bbpax.keeper.security.model.User;

import static ru.bbpax.keeper.security.model.AccessLevels.ALL;

@Slf4j
public class NotesMethodSecurityOperations
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private Object target;


    public NotesMethodSecurityOperations(Authentication authentication) {
        super(authentication);
    }

    public boolean isNoteOwner(String noteId) {
        log.info("verify if user is owner of {}", noteId);
        if (!(this.getPrincipal() instanceof CustomUserPrincipal)) return false;
        CustomUserPrincipal principal = ((CustomUserPrincipal) this.getPrincipal());
        User user = principal.getUser();
        return user.getPrivileges().contains(new Privilege(noteId, ALL));
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
