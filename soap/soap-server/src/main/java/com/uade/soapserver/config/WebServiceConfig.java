package com.uade.soapserver.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Configuración central del servidor SOAP.
 *
 * @EnableWs activa el soporte de Spring Web Services.
 *
 * Flujo de una petición SOAP:
 *   Cliente HTTP → MessageDispatcherServlet (/ws/*) → CountryEndpoint
 *
 * El WSDL generado automáticamente se expone en:
 *   http://localhost:8080/ws/countries.wsdl
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    /**
     * Registra el servlet de Spring-WS para que intercepte todas las
     * peticiones bajo /ws/*.
     * setTransformWsdlLocations(true) ajusta la URL del WSDL según el host
     * desde donde se acceda (útil en Docker o entornos con proxy).
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    /**
     * Genera el WSDL automáticamente a partir del XSD.
     * El nombre del Bean ("countries") determina la URL del WSDL:
     *   → /ws/countries.wsdl
     */
    @Bean(name = "countries")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CountriesPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://uade.com/soap/countries");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    /**
     * Carga el XSD desde el classpath para que DefaultWsdl11Definition
     * pueda construir el WSDL.
     */
    @Bean
    public XsdSchema countriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("countries.xsd"));
    }
}
