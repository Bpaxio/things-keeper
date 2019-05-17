package ru.bbpax.keeper.configurarion.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import ru.bbpax.keeper.security.service.NotesExpressionHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        NotesExpressionHandler expressionHandler = new NotesExpressionHandler();

//        expressionHandler.setPermissionEvaluator(new NotesPermissionEvaluator());
        return expressionHandler;
    }
}
