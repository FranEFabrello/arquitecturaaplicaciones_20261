package com.uade.modernizacion.auth.domain;

public class Usuario {

    private final Long id;
    private final String username;
    private final String passwordHash;
    private final String nombre;
    private final String rol;

    public Usuario(Long id, String username, String passwordHash, String nombre, String rol) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombre = nombre;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }
}

