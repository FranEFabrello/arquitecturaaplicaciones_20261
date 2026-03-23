package com.uade.modernizacion.auth.api.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String username,
        String nombre,
        String rol
) {
}

