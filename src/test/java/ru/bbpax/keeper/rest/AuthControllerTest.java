package ru.bbpax.keeper.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.bbpax.keeper.rest.security.AuthRequest;
import ru.bbpax.keeper.rest.security.AuthResponse;
import ru.bbpax.keeper.security.service.UserAuthService;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest
class AuthControllerTest {
    private static final String TOKEN = "validToken))";

    @Configuration
    @Import({ AuthController.class, MockMvcSecurityConfig.class })
    static class Config {
    }
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserAuthService authService;

    @Test
    void login() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("user");
        request.setPassword("password");

        doReturn(response(request.getUsername()))
                .when(authService)
                .login(request);
        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(post("/auth/login/")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(request.getUsername())))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.token", is(TOKEN)));
    }

    @Test
    void register() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("user");
        request.setPassword("password");

        doReturn(response(request.getUsername()))
                .when(authService)
                .register(request);
        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(post("/auth/register/")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(request.getUsername())))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.token", is(TOKEN)));
    }

    private AuthResponse response(String username) {
        return AuthResponse.builder().username(username).token(TOKEN).build();
    }
}