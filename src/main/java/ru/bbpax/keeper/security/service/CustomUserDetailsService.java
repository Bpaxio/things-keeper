package ru.bbpax.keeper.security.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.security.model.CustomUserPrincipal;
import ru.bbpax.keeper.security.model.User;
import ru.bbpax.keeper.security.repo.UserRepo;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepo repo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("try to find User: {}", username);
        return repo.findByUsername(username)
                .map(CustomUserPrincipal::new) //???
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
