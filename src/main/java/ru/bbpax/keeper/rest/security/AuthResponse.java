package ru.bbpax.keeper.rest.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String username;
    private String token;
}
