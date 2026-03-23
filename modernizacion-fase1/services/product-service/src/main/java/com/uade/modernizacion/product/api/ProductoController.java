package com.uade.modernizacion.product.api;

import com.uade.modernizacion.product.api.dto.ProductoRequest;
import com.uade.modernizacion.product.domain.Producto;
import com.uade.modernizacion.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductService productService;

    public ProductoController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Producto> getProductos() {
        return productService.listAll();
    }

    @GetMapping("/{id}")
    public Producto getProducto(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto createProducto(@Valid @RequestBody ProductoRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public Producto updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducto(@PathVariable Long id) {
        productService.delete(id);
    }
}

