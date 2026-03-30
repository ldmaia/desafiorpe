package br.com.rpe.holder.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.in-memory")
public record InMemoryUserProperties(
        String username,
        String password,
        String role
) {
}
