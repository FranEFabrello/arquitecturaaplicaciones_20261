package com.uade.soapclient.config;

import com.uade.soapclient.client.CountryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Configura los beans necesarios para que el cliente SOAP funcione.
 *
 * Jaxb2Marshaller: convierte objetos Java ↔ XML (serialización/deserialización).
 *   - contextPath: paquete donde están las clases generadas por el XSD.
 *
 * CountryClient: recibe el marshaller y la URL del servidor para enviar peticiones.
 */
@Configuration
public class SoapClientConfig {

    @Value("${soap.server.url}")
    private String serverUrl;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // Paquete generado por jaxb2-maven-plugin a partir del namespace del XSD
        marshaller.setContextPath("com.uade.soap.countries");
        return marshaller;
    }

    @Bean
    public CountryClient countryClient(Jaxb2Marshaller marshaller) {
        CountryClient client = new CountryClient();
        client.setDefaultUri(serverUrl);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
