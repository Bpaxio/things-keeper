package ru.bbpax.keeper.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import ru.bbpax.keeper.model.AbstractNote;
import ru.bbpax.keeper.security.model.CustomUserPrincipal;
import ru.bbpax.keeper.security.model.Privilege;
import ru.bbpax.keeper.security.model.User;

import java.util.Collections;

import static ru.bbpax.keeper.security.model.AccessLevels.DELETE;
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


    public boolean hasReadPrivileges(AbstractNote note) {
        return hasPrivileges(note, new Privilege(note.getId(), Collections.singleton(READ)));
    }

    public boolean hasWritePrivileges(AbstractNote note) {
        return hasPrivileges(note, new Privilege(note.getId(), Collections.singleton(WRITE)));
    }

    public boolean hasDeletePrivileges(AbstractNote note) {
        return hasPrivileges(note, new Privilege(note.getId(), Collections.singleton(DELETE)));
    }

    public boolean hasSharePrivileges(AbstractNote note) {
        return hasPrivileges(note, new Privilege(note.getId(), Collections.singleton(SHARE)));
    }

    public boolean hasPrivileges(AbstractNote note, Privilege privilege) {
        log.info("verify if user has privilege {} for {}", privilege, note);
        if (!(this.getPrincipal() instanceof CustomUserPrincipal)) return false;
        if (isNoteOwner(note)) return true;
        User user = ((CustomUserPrincipal) this.getPrincipal()).getUser();
        return user.getPrivileges().contains(privilege);
    }

    public boolean isNoteOwner(AbstractNote note) {
        log.info("verify if user is owner of {}", note);
        if (!(this.getPrincipal() instanceof CustomUserPrincipal)) return false;
        User user = ((CustomUserPrincipal) this.getPrincipal()).getUser();
        return note.getCreatedBy().equals(user.getId());
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
