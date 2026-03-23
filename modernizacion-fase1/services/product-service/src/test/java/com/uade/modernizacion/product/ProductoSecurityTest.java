package com.uade.modernizacion.product;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoSecurityTest {

    private static final String SECRET = "change-me-please-change-me-please-change-me-2026";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProductosSinTokenDebeRetornar401() throws Exception {
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void postConRolUserDebeRetornar403() throws Exception {
        String userToken = token("usuario", "USER");

        mockMvc.perform(post("/api/v1/productos")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Nuevo\",\"descripcion\":\"Demo\",\"precio\":10.5,\"stock\":1,\"categoria\":\"Otros\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void postConRolAdminDebeRetornar201() throws Exception {
        String adminToken = token("admin", "ADMIN");

        mockMvc.perform(post("/api/v1/productos")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Nuevo\",\"descripcion\":\"Demo\",\"precio\":10.5,\"stock\":1,\"categoria\":\"Otros\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    private String token(String username, String rol) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claims(Map.of("rol", rol, "nombre", username))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(1800)))
                .signWith(key)
                .compact();
    }
}

