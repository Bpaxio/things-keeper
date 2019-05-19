package ru.bbpax.keeper.service;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bbpax.keeper.security.service.PrivilegeService;

@Configuration
public class CommonConfiguration {
    @MockBean
    private PrivilegeService privilegeService;
    @MockBean
    private FilterService filterService;

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }
}
