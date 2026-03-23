package com.uade.modernizacion.product.service;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Producto no encontrado: " + id);
    }
}

