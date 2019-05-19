package ru.bbpax.keeper.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.bbpax.keeper.configurarion.security.GlobalMethodSecurityConfig;
import ru.bbpax.keeper.configurarion.security.WebSecurityConfig;
import ru.bbpax.keeper.security.model.CustomUserPrincipal;
import ru.bbpax.keeper.security.model.User;
import ru.bbpax.keeper.security.token.TokenProvider;

import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static ru.bbpax.keeper.configurarion.security.WebSecurityConfig.ENCODING_STRENGTH;
import static ru.bbpax.keeper.security.model.Roles.USER;

@Configuration
@Import({ WebSecurityConfig.class,
        GlobalMethodSecurityConfig.class })
public class MockMvcSecurityConfig {
    @Bean("customUserDetailsService")
    public UserDetailsService userDetailsService() {
        UserDetailsService mock = mock(UserDetailsService.class);
        User user = new User();
        user.setAuthorities(Collections.singleton(new SimpleGrantedAuthority(USER)));
        user.setUsername("user");
        user.setPassword(new BCryptPasswordEncoder(ENCODING_STRENGTH).encode("password"));
        doReturn(new CustomUserPrincipal(user)).when(mock).loadUserByUsername(user.getUsername());
        return mock;
    }

    @Bean
    public TokenProvider tokenProvider(@Qualifier("customUserDetailsService")
                                               UserDetailsService userDetailsService) {
        return new TokenProvider(userDetailsService);
    }
}
