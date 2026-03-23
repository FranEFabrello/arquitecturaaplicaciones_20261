# Comparación: Legacy vs Modernización

## 📊 Resumen visual

### Arquitectura Legacy
```
┌─────────────────────────────────────┐
│         Monolito WAR (Tomcat 7)     │
│                                     │
│  ┌─────────────────────────────┐   │
│  │  Servlets (RequestHandlers) │   │
│  │  - LoginServlet             │   │
│  │  - LogoutServlet            │   │
│  │  - ProductoServlet          │   │
│  └─────────────────────────────┘   │
│                │                    │
│  ┌─────────────▼─────────────┐   │
│  │  DAOs (Data Access)       │   │
│  │  - UsuarioDAO             │   │
│  │  - ProductoDAO            │   │
│  └─────────────▼─────────────┘   │
│                │                    │
│  ┌─────────────▼─────────────┐   │
│  │  H2 Database (In-Memory)  │   │
│  │  - usuarios               │   │
│  │  - productos              │   │
│  └───────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │  JSP Views (Server-side)    │   │
│  │  - login.jsp                │   │
│  │  - productos/lista.jsp      │   │
│  │  - productos/formulario.jsp │   │
│  └─────────────────────────────┘   │
│                                     │
│  Security: Sessions + AuthFilter    │
│  Java: 11, Spring: NO               │
└─────────────────────────────────────┘

Client → HTTP → Monolito → HTML/JSP
```

### Arquitectura Modernización
```
┌──────────────────────────────┐    ┌──────────────────────────────┐
│    Auth Service              │    │    Product Service           │
│    (Spring Boot 3 - 8081)    │    │    (Spring Boot 3 - 8082)    │
│                              │    │                              │
│  ┌─────────────────────┐     │    │  ┌─────────────────────┐    │
│  │  @RestController    │     │    │  │  @RestController    │    │
│  │  POST /auth/login   │     │    │  │  GET /productos     │    │
│  └──────────┬──────────┘     │    │  │  POST /productos    │    │
│             │                │    │  │  PUT /productos/{id}│    │
│  ┌──────────▼──────────┐     │    │  │  DELETE /productos  │    │
│  │ AuthService         │     │    │  └──────────┬──────────┘    │
│  │ - JWT Generation    │     │    │             │                │
│  │ - Credential Check  │     │    │  ┌──────────▼──────────┐    │
│  └──────────┬──────────┘     │    │  │ ProductService      │    │
│             │                │    │  │ - CRUD Operations   │    │
│  ┌──────────▼──────────┐     │    │  │ - Validation        │    │
│  │ UserService         │     │    │  └──────────┬──────────┘    │
│  │ - In-Memory Store   │     │    │             │                │
│  │ - BCrypt Hashing    │     │    │  ┌──────────▼──────────┐    │
│  └─────────────────────┘     │    │  │ SecurityConfig      │    │
│                              │    │  │ - JWT Filter        │    │
│  Security: JWT Bearer        │    │  │ - Role-based Access │    │
│  Java: 17, Spring: 3.2       │    │  └─────────────────────┘    │
│  Testing: JUnit 5            │    │                              │
│  Actuator: Health            │    │  Security: JWT Bearer        │
└──────────────────────────────┘    │  Java: 17, Spring: 3.2      │
                                    │  Testing: JUnit 5            │
        ▲                           │  Actuator: Health            │
        │                           └──────────────────────────────┘
        │
        │ JWT Token Bearer
        │
    Client → HTTP/REST → Services → JSON Responses
```

---

## 🔄 Flujo de solicitud

### Legacy
```
1. Cliente accede http://localhost:8080/legacy
2. AuthFilter verifica sesión HTTP
3. LoginServlet maneja autenticación (BCrypt check)
4. Guarda Usuario en sesión HTTP
5. Devuelve JSP renderizada con datos server-side
6. Cliente navega a /productos (GET request)
7. ProductoServlet consulta ProductoDAO
8. ProductoDAO accede a BD H2
9. Servlet carga lista.jsp con datos
10. Devuelve HTML renderizado
```

### Modernización
```
1. Cliente hace POST a http://localhost:8081/api/v1/auth/login
   {
     "username": "admin",
     "password": "admin123"
   }

2. AuthController → AuthService → UserService
   - Busca usuario por username
   - Verifica password con BCrypt
   - Si es válido, genera JWT

3. Devuelve JSON:
   {
     "accessToken": "eyJhbGc...",
     "tokenType": "Bearer",
     "expiresIn": 1800,
     "username": "admin",
     "nombre": "Administrador",
     "rol": "ADMIN"
   }

4. Cliente hace GET a http://localhost:8082/api/v1/productos
   Header: Authorization: Bearer eyJhbGc...

5. JwtAuthenticationFilter valida token
   - Extrae claims (username, rol)
   - Configura SecurityContext con rol

6. SecurityConfig verifica permiso (ROLE_ADMIN, ROLE_USER)

7. ProductoController devuelve JSON:
   [
     {
       "id": 1,
       "nombre": "Laptop HP",
       "precio": 850.00,
       ...
     }
   ]

8. Cliente procesa JSON y renderiza en front-end
   (React/Vue/Angular)
```

---

## 📈 Comparación de características

| Característica | Legacy | Modernización |
|---|---|---|
| **Arquitectura** | Monolito Servlet | Microservicios REST |
| **Protocolo** | HTTP (Sesiones) | HTTP (JWT) |
| **Respuestas** | HTML/JSP renderizado | JSON |
| **Java** | 11 | 17+ |
| **Framework** | Servlet API | Spring Boot 3 |
| **Seguridad** | Session-based | Token-based JWT |
| **Roles** | Almacenados en sesión | En claims JWT |
| **Escalabilidad** | Difícil (sesiones estado) | Fácil (stateless) |
| **Tests** | Ninguno | JUnit 5 + MockMvc |
| **Health checks** | Manual | Actuator `/health` |
| **Observabilidad** | Logs básicos | Actuator + metrics |
| **Contenedores** | No | Docker + Compose |
| **Persistencia** | H2 memoria | H2 memoria (upgradeable) |
| **Validación** | Manual en servlet | Jakarta Validation |
| **Error handling** | JSP error pages | REST Problem Details |

---

## 🔐 Comparación de seguridad

### Legacy: Session-based
```
Lado servidor:
┌──────────────────────────────┐
│  Session Storage (memoria)   │
│  Session1: {                 │
│    usuarioLogueado: Usuario  │
│    createdAt: timestamp      │
│  }                           │
└──────────────────────────────┘

Cliente:
┌─────────────────────────────────────┐
│  Cookie: JSESSIONID=ABC123          │
│  (HTTP Session ID opaco)            │
└─────────────────────────────────────┘

Pros:
+ Datos sensibles en servidor
+ Logout inmediato (invalidar sesión)
+ Control total servidor

Contras:
- Requiere sticky sessions en clusters
- Escalabilidad limitada
- No funciona bien en microservicios
- Sesión puede expirar sin sincronización
```

### Modernización: JWT-based
```
Cliente recibe JWT:
┌──────────────────────────────────────┐
│  {                                   │
│    "sub": "admin",                   │
│    "rol": "ADMIN",                   │
│    "nombre": "Administrador",        │
│    "iat": 1711270000,                │
│    "exp": 1711271800                 │
│  }                                   │
│  Firmado con HMAC-SHA256             │
└──────────────────────────────────────┘

Cliente envía en cada request:
┌─────────────────────────────────────────┐
│  Authorization: Bearer eyJhbGc...       │
└─────────────────────────────────────────┘

Servidor valida:
1. Verifica firma con secreto compartido
2. Valida expiración (exp claim)
3. Extrae información de claims
4. No necesita buscar en BD ni sesión

Pros:
+ Stateless (no requiere sesión servidor)
+ Escalable horizontalmente
+ Ideal para microservicios/APIs
+ Token contiene información útil
+ CORS-friendly
+ Puede ser usado entre múltiples dominios

Contras:
- Token expirado no se invalida inmediatamente
  (solución: refresh tokens, blacklist)
- Secreto debe estar sincronizado entre servicios
- Tamaño payload visible (no encriptado, solo firmado)
```

---

## 🚀 Ventajas de la modernización

### 1. **Separación de responsabilidades**
   - Auth Service: solo autenticación
   - Product Service: solo gestión de productos
   - Cada servicio escalable independientemente

### 2. **Escalabilidad horizontal**
   - Sin estado en servidor (stateless)
   - Múltiples instancias sin sesiones compartidas
   - Load balancing sencillo

### 3. **Compatibilidad con frontend moderno**
   - APIs REST con JSON
   - CORS soportado
   - Compatible con SPA (React/Vue/Angular)
   - Consumible desde móviles/desktop

### 4. **Testing**
   - Tests unitarios con Mockito
   - Tests de integración con MockMvc
   - Fácil testear controladores/servicios

### 5. **Operaciones**
   - Docker compatible
   - Health checks estándar
   - Actuator con métricas
   - Logs estructurados posibles

### 6. **Seguridad moderna**
   - JWT estándar (RFC 7519)
   - Tokens con expiración
   - Información en claims (extensible)
   - Refresh tokens posibles en Fase 2

---

## 🔄 Migración del estado

### Legacy: Usuario en sesión HTTP
```java
// Login servlet
Usuario user = usuarioDAO.autenticar(username, password);
HttpSession session = req.getSession(true);
session.setAttribute("usuarioLogueado", user);
session.setMaxInactiveInterval(30 * 60);  // 30 minutos

// Acceso posterior
Usuario user = (Usuario) req.getSession().getAttribute("usuarioLogueado");
```

### Modernización: Usuario en JWT
```java
// Auth service
Usuario user = userService.findByUsername(username)
  .filter(u -> userService.matches(password, u.getPasswordHash()))
  .orElseThrow(...);

String token = jwtService.generateToken(
  user.getUsername(),
  Map.of("rol", user.getRol(), "nombre", user.getNombre())
);

// Product service: validación en filtro
JwtAuthenticationFilter → extrae claims → set SecurityContext
```

---

## 📊 Diagrama de deployment

### Legacy (Producción)
```
Load Balancer
    │
    ├─→ Tomcat Server 1 (puerto 8080)
    │   └─ legacy.war
    │
    ├─→ Tomcat Server 2 (puerto 8080)
    │   └─ legacy.war
    │
    └─→ Tomcat Server 3 (puerto 8080)
        └─ legacy.war

Problema: Sticky sessions necesario (cada usuario a mismo servidor)
           H2 en memoria no sincronizada entre servidores
           Escalabilidad limitada
```

### Modernización (Producción)
```
API Gateway (Spring Cloud Gateway)
    │
    ├─→ Auth Service (puerto 8081)
    │   ├─ auth-service-1:8081
    │   ├─ auth-service-2:8081
    │   └─ auth-service-3:8081
    │
    └─→ Product Service (puerto 8082)
        ├─ product-service-1:8082
        ├─ product-service-2:8082
        └─ product-service-3:8082

Ventaja: Stateless, sin sticky sessions
         Cada instancia es idéntica
         Escalable sin sincronización
         Fácil agregar/quitar instancias
```

---

## 🎯 Conclusión

La **modernización de legacy a microservicios** permite:

1. ✅ Separar dominios funcionales
2. ✅ Escalar independientemente
3. ✅ Adoptar estándares REST/JSON
4. ✅ Prepararse para arquitectura cloud-native
5. ✅ Mejorar testabilidad y mantenibilidad
6. ✅ Facilitar migración a nuevas tecnologías

**Esta Fase 1 sienta las bases. Fase 2 agregará persistencia real y API Gateway.**

