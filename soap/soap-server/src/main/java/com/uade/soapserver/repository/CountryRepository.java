package com.uade.soapserver.repository;

import com.uade.soap.countries.Country;
import com.uade.soap.countries.Currency;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Repositorio con datos hardcoded que simula una base de datos.
 * En un proyecto real esto se reemplazaría por un JpaRepository o similar.
 *
 * Las clases Country y Currency son generadas por el plugin jaxb2-maven-plugin
 * a partir de countries.xsd. Se encuentran en target/generated-sources/jaxb.
 */
@Component
public class CountryRepository {

    private static final Map<String, Country> countries = new HashMap<>();

    @PostConstruct
    public void initData() {
        Country argentina = buildCountry("Argentina", "Buenos Aires", 45376763, Currency.ARS);
        Country spain     = buildCountry("Spain",     "Madrid",       47415750, Currency.EUR);
        Country uk        = buildCountry("United Kingdom", "London",  67215293, Currency.GBP);
        Country poland    = buildCountry("Poland",    "Warsaw",       37846611, Currency.PLN);
        Country usa       = buildCountry("United States", "Washington D.C.", 331449281, Currency.USD);

        for (Country c : new Country[]{argentina, spain, uk, poland, usa}) {
            countries.put(c.getName(), c);
        }
    }

    public Country findCountry(String name) {
        return countries.get(name);
    }

    private Country buildCountry(String name, String capital, int population, Currency currency) {
        Country country = new Country();
        country.setName(name);
        country.setCapital(capital);
        country.setPopulation(population);
        country.setCurrency(currency);
        return country;
    }
}
