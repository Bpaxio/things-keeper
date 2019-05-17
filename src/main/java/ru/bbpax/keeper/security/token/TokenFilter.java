package ru.bbpax.keeper.security.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import ru.bbpax.keeper.security.token.error.NotAuthorizedException;
import ru.bbpax.keeper.security.token.smartdec.TokenServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class TokenFilter extends GenericFilterBean {
    private static final String BEARER = "Bearer ";

    private final TokenProvider tokenProvider;

    public TokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        try {
        String token = Optional
                .ofNullable(((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION))
                .map(s -> s.substring(BEARER.length()))
                .orElseThrow(NotAuthorizedException::new);

        if (tokenProvider.validateToken(token)) {
            Authentication auth = tokenProvider.getAuthentication(token);
            if(auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        } catch (NotAuthorizedException e) {
            log.warn("Not Authorized");
        }

        filterChain.doFilter(request, response);
    }
}
