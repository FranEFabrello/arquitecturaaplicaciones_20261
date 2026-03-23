package com.uade.modernizacion.auth.service;

import com.uade.modernizacion.auth.domain.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final Map<String, Usuario> usersByUsername = new ConcurrentHashMap<>();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService() {
        // Seeds compatibles con el sistema legacy.
        usersByUsername.put("admin", new Usuario(1L, "admin", encoder.encode("admin123"), "Administrador", "ADMIN"));
        usersByUsername.put("usuario", new Usuario(2L, "usuario", encoder.encode("user123"), "Usuario Demo", "USER"));
    }

    public Optional<Usuario> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    public boolean matches(String rawPassword, String passwordHash) {
        return encoder.matches(rawPassword, passwordHash);
    }
}

