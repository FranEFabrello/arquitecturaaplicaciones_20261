package com.uade.modernizacion.product.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String descripcion,
        @DecimalMin(value = "0.0", message = "El precio no puede ser negativo") double precio,
        @PositiveOrZero(message = "El stock no puede ser negativo") int stock,
        String categoria
) {
}

