package com.uade.soapclient.client;

import com.uade.soap.countries.GetCountryRequest;
import com.uade.soap.countries.GetCountryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

/**
 * Cliente SOAP que consume el servicio del soap-server.
 *
 * WebServiceGatewaySupport es la clase base de Spring-WS para clientes.
 * Proporciona el WebServiceTemplate preconfigurado con marshaller/unmarshaller.
 *
 * El bean de esta clase se crea en SoapClientConfig, donde se inyectan
 * la URL del servidor y el Jaxb2Marshaller.
 */
public class CountryClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(CountryClient.class);

    /**
     * Envía una petición SOAP al servidor y retorna el objeto Country deserializado.
     *
     * @param countryName nombre del país a consultar (ej: "Spain", "Argentina")
     * @return respuesta con el objeto Country o country=null si no existe
     */
    public GetCountryResponse getCountry(String countryName) {
        GetCountryRequest request = new GetCountryRequest();
        request.setName(countryName);

        log.info(">>> Enviando petición SOAP para: '{}'", countryName);

        GetCountryResponse response = (GetCountryResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);

        if (response.getCountry() != null) {
            log.info("<<< Respuesta recibida: {} - Capital: {}",
                    response.getCountry().getName(),
                    response.getCountry().getCapital());
        } else {
            log.warn("<<< El servidor no encontró el país: '{}'", countryName);
        }

        return response;
    }
}
