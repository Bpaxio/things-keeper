package ru.bbpax.keeper.security.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.rest.security.AuthRequest;
import ru.bbpax.keeper.rest.security.AuthResponse;
import ru.bbpax.keeper.security.error.UsernameTakenException;
import ru.bbpax.keeper.security.error.WrongPasswordException;
import ru.bbpax.keeper.security.error.WrongUsernameException;
import ru.bbpax.keeper.security.model.User;
import ru.bbpax.keeper.security.repo.UserRepo;
import ru.bbpax.keeper.security.token.TokenProvider;

import java.util.Collections;

import static ru.bbpax.keeper.security.model.Roles.USER;
import static ru.bbpax.keeper.util.Helper.isBlank;

/**
 * @author Vlad Rakhlinskii
 * Created on 17.05.2019.
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider provider;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(AuthRequest auth) {
        String username = auth.getUsername();
        String password = auth.getPassword();
        if (isBlank(username))
            throw new WrongUsernameException(username);
        if (isBlank(password))
            throw new WrongPasswordException();
        if (userRepo.findByUsername(username).isPresent()) {
            throw new UsernameTakenException(username);
        }
        log.info("start register");
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAuthorities(Collections.singleton(new SimpleGrantedAuthority(USER)));
        userRepo.save(user);
        return login(auth);
    }

    public AuthResponse login(AuthRequest auth) {
        String username = auth.getUsername();
        try {
            log.info("start auth");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, auth.getPassword()));
            log.info("success");
            String token = provider.createToken(username);
            log.info("token created");
            return AuthResponse.builder()
                    .username(username)
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/pass");
        }
    }
}
