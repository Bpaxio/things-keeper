package ru.bbpax.keeper.rest;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bbpax.keeper.rest.security.AuthRequest;
import ru.bbpax.keeper.rest.security.AuthResponse;
import ru.bbpax.keeper.security.service.UserAuthService;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {
    private final UserAuthService service;

    @PostMapping("login")
    @ApiOperation("login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest auth) {
        log.info("{}'s attempt to login", auth.getUsername());
        return ok(service.login(auth));
    }

    @PostMapping("register")
    @ApiOperation("register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest auth) {
        log.info("{}'s attempt to login", auth.getUsername());
        return ok(service.register(auth));
    }
}
