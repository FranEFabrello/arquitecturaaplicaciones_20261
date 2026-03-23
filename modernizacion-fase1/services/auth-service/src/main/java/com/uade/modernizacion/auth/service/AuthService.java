package com.uade.modernizacion.auth.service;

import com.uade.modernizacion.auth.api.dto.LoginResponse;
import com.uade.modernizacion.auth.domain.Usuario;
import com.uade.modernizacion.auth.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public LoginResponse login(String username, String password) {
        Usuario user = userService.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas."));

        if (!userService.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas.");
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                Map.of("rol", user.getRol(), "nombre", user.getNombre())
        );

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                user.getUsername(),
                user.getNombre(),
                user.getRol()
        );
    }
}

