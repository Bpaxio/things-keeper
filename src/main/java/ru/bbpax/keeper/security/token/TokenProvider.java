package ru.bbpax.keeper.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.bbpax.keeper.security.model.CustomUserPrincipal;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {
    @Value("${security.token.secret:!@D$sa%6def^SdtWf67gfw4vbr4dhj4s}")
    private String secret;
    @Value("${security.token.expire-time:600000}")
    private long tokenLife = 400000; //ms

    private final UserDetailsService service;

    @Autowired
    public TokenProvider(@Qualifier("customUserDetailsService") UserDetailsService service) {
        this.service = service;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String username) {
        CustomUserPrincipal userDetails = (CustomUserPrincipal)service.loadUserByUsername(username);
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.putIfAbsent("privileges", userDetails.getPrivileges());
        claims.putIfAbsent("roles", userDetails.getAuthorities());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenLife);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        log.info("token [{}]\nfor user:\n{}", token, userDetails);
        return token;
    }

    public boolean validateToken(String token) {
        try {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return !claims.getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token is not valid", e);
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = service.loadUserByUsername(getUsername(token));
        log.info("get Auth: {}", userDetails);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
