# Demo SOAP - Aplicaciones Interactivas (UADE 2026)

Proyecto de ejemplo que implementa una arquitectura productor/consumidor usando el protocolo **SOAP** sobre Spring Boot.

## Estructura

```
pruebas/
├── soap-server/      # Servidor SOAP (productor) — puerto 8080
├── soap-client/      # Cliente SOAP con API REST (consumidor) — puerto 8081
└── docker-compose.yml
```

## Requisitos

- **Java 21**
- **Maven 3.6+**

Verificá que estén instalados:

```bash
java -version
mvn -version
```

---

## Cómo correr sin Docker

Necesitás **dos terminales abiertas** al mismo tiempo.

### Terminal 1 — Levantar el servidor

```bash
cd soap-server
mvn spring-boot:run
```

Esperá hasta ver en la consola:

```
Started SoapServerApplication
```

### Terminal 2 — Levantar el cliente

```bash
cd soap-client
mvn spring-boot:run
```

Esperá hasta ver:

```
Started SoapClientApplication
```

> La primera vez Maven descarga las dependencias, puede tardar unos minutos.

---

## Probar el servicio

Una vez que ambos estén corriendo, abrí el navegador o usá curl.

### Consultar un país (API REST del cliente)

```
GET http://localhost:8081/client/country/{nombre}
```

**Países disponibles:**

| Nombre | Capital | Moneda |
|---|---|---|
| Argentina | Buenos Aires | ARS |
| Spain | Madrid | EUR |
| United Kingdom | London | GBP |
| Poland | Warsaw | PLN |
| United States | Washington D.C. | USD |

**Ejemplos:**

```bash
curl http://localhost:8081/client/country/Spain
curl http://localhost:8081/client/country/Argentina
curl "http://localhost:8081/client/country/United%20Kingdom"
```

**Respuesta de ejemplo:**

```json
{
  "name": "Spain",
  "capital": "Madrid",
  "population": 47415750,
  "currency": "EUR"
}
```

Si el país no existe, devuelve `404 Not Found`.

### WSDL del servidor

```
http://localhost:8080/ws/countries.wsdl
```

---

## Cómo correr con Docker

Si tenés Docker disponible, desde la raíz del proyecto:

```bash
docker-compose up --build
```

Esto levanta ambos servicios automáticamente. El cliente espera a que el servidor esté saludable antes de arrancar.

---

## Arquitectura

```
Navegador / curl
      │
      │  GET /client/country/{name}
      ▼
┌─────────────┐        Mensaje SOAP (XML)       ┌─────────────┐
│ soap-client │  ─────────────────────────────► │ soap-server │
│  :8081      │ ◄─────────────────────────────  │  :8080      │
└─────────────┘      Respuesta SOAP (XML)        └─────────────┘
```

El cliente expone una API REST simple que internamente construye y envía mensajes SOAP al servidor.
