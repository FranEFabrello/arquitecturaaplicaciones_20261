package com.uade.soapclient.controller;

import com.uade.soap.countries.Country;
import com.uade.soap.countries.GetCountryResponse;
import com.uade.soapclient.client.CountryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador REST de demostración.
 *
 * Expone un endpoint HTTP simple que internamente llama al servicio SOAP.
 * Esto permite probar el flujo completo con un simple GET desde el navegador
 * o con curl/Postman, sin necesidad de construir un sobre SOAP manualmente.
 *
 * Flujo: GET /client/country/{name} → CountryClient → SOAP → soap-server
 */
@RestController
@RequestMapping("/client")
public class CountryController {

    private final CountryClient countryClient;

    @Autowired
    public CountryController(CountryClient countryClient) {
        this.countryClient = countryClient;
    }

    /**
     * Ejemplo de uso:
     *   GET http://localhost:8081/client/country/Spain
     *   GET http://localhost:8081/client/country/Argentina
     *   GET http://localhost:8081/client/country/United%20Kingdom
     */
    @GetMapping("/country/{name}")
    public ResponseEntity<?> getCountry(@PathVariable String name) {
        GetCountryResponse response = countryClient.getCountry(name);
        Country country = response.getCountry();

        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name",       country.getName());
        result.put("capital",    country.getCapital());
        result.put("population", country.getPopulation());
        result.put("currency",   country.getCurrency().value());

        return ResponseEntity.ok(result);
    }
}
