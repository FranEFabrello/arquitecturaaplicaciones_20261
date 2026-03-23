package com.uade.soapserver.endpoint;

import com.uade.soap.countries.GetCountryRequest;
import com.uade.soap.countries.GetCountryResponse;
import com.uade.soapserver.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Endpoint SOAP: punto de entrada para las peticiones del servicio.
 *
 * @Endpoint      → marca la clase como manejadora de mensajes SOAP (equivalente a @Controller).
 * @PayloadRoot   → enruta la petición según el namespace y el elemento raíz del XML.
 * @RequestPayload → deserializa el XML de entrada a un objeto Java (JAXB Unmarshalling).
 * @ResponsePayload → serializa el objeto Java de retorno a XML (JAXB Marshalling).
 */
@Endpoint
public class CountryEndpoint {

    private static final String NAMESPACE_URI = "http://uade.com/soap/countries";

    private final CountryRepository countryRepository;

    @Autowired
    public CountryEndpoint(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Maneja peticiones cuyo payload tiene el elemento raíz <getCountryRequest>
     * en el namespace http://uade.com/soap/countries.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        GetCountryResponse response = new GetCountryResponse();
        response.setCountry(countryRepository.findCountry(request.getName()));
        return response;
    }
}
