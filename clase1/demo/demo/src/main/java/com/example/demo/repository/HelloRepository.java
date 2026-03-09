package com.example.demo.repository;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HelloRepository {
    private final List<String> nombres = new ArrayList<>();

    public HelloRepository() {
        nombres.add("Juan");
        nombres.add("Pedro");
        nombres.add("Marta");
    }

    public List<String> findAll() {
        return nombres;
    }

    public void add(String nombre) {
        nombres.add(nombre);
    }

    public boolean remove(String nombre) {
        return nombres.remove(nombre);
    }
}
