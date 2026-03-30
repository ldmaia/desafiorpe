package br.com.rpe.holder.adapters.in.rest;

public record AuthResponse(
        String accessToken,
        String tokenType
) {
}
