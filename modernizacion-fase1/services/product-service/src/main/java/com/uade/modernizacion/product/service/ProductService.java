package com.uade.modernizacion.product.service;

import com.uade.modernizacion.product.api.dto.ProductoRequest;
import com.uade.modernizacion.product.domain.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Producto> products = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(4);

    public ProductService() {
        products.put(1L, new Producto(1L, "Laptop HP", "Laptop HP 15 pulgadas Intel Core i5", 850.00, 10, "Electronica"));
        products.put(2L, new Producto(2L, "Mouse Inalambrico", "Mouse optico inalambrico USB", 25.00, 50, "Perifericos"));
        products.put(3L, new Producto(3L, "Teclado Mecanico", "Teclado mecanico RGB retroiluminado", 75.00, 30, "Perifericos"));
    }

    public List<Producto> listAll() {
        List<Producto> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparing(Producto::getId));
        return list;
    }

    public Producto getById(Long id) {
        Producto producto = products.get(id);
        if (producto == null) {
            throw new ProductNotFoundException(id);
        }
        return producto;
    }

    public Producto create(ProductoRequest request) {
        Long id = sequence.getAndIncrement();
        Producto producto = mapToProducto(id, request);
        products.put(id, producto);
        return producto;
    }

    public Producto update(Long id, ProductoRequest request) {
        if (!products.containsKey(id)) {
            throw new ProductNotFoundException(id);
        }
        Producto updated = mapToProducto(id, request);
        products.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        if (products.remove(id) == null) {
            throw new ProductNotFoundException(id);
        }
    }

    private Producto mapToProducto(Long id, ProductoRequest request) {
        return new Producto(
                id,
                request.nombre().trim(),
                request.descripcion(),
                request.precio(),
                request.stock(),
                request.categoria()
        );
    }
}

