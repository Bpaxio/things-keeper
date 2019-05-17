package ru.bbpax.keeper.rest;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bbpax.keeper.rest.security.AuthRequest;
import ru.bbpax.keeper.rest.security.AuthResponse;
import ru.bbpax.keeper.security.token.TokenProvider;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider provider;

    @PostMapping("login")
    @ApiOperation("login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest auth) {
        String username = auth.getUsername();
        try {
            log.info("start auth");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, auth.getPassword()));
            log.info("success");
            String token = provider.createToken(username);
            log.info("token created");
            return ok(AuthResponse.builder()
                    .username(username)
                    .token(token)
                    .build());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/pass");
        }
    }
}
