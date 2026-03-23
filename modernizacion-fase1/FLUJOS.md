# 🎯 Flujo de ejecución - Modernizacion Fase 1

## Flujo 1: Autenticación (Login)

```
┌─────────────┐
│   Cliente   │
│  (Browser/  │
│   Postman)  │
└──────┬──────┘
       │
       │ POST /api/v1/auth/login
       │ {"username":"admin","password":"admin123"}
       │
       ▼
┌──────────────────────────────────────────────┐
│         AUTH SERVICE (Puerto 8081)           │
│                                              │
│  AuthController                              │
│  ├─ POST /api/v1/auth/login                 │
│  │   └─> AuthService.login()                │
│  │       └─> UserService.findByUsername()   │
│  │           └─> Map<username, Usuario>     │
│  │               (en memoria)                │
│  │                                          │
│  │       ├─ Verifica password con BCrypt    │
│  │       │  userService.matches(raw, hash)  │
│  │       │                                  │
│  │       └─ Genera JWT con JwtService       │
│  │          jwtService.generateToken(       │
│  │            subject="admin",              │
│  │            claims={                      │
│  │              "rol":"ADMIN",              │
│  │              "nombre":"Administrador"    │
│  │            }                             │
│  │          )                               │
│  │                                          │
│  ├─ Retorna LoginResponse:                 │
│  │  {                                       │
│  │    "accessToken": "eyJhbGc...",         │
│  │    "tokenType": "Bearer",                │
│  │    "expiresIn": 1800,                    │
│  │    "username": "admin",                  │
│  │    "nombre": "Administrador",            │
│  │    "rol": "ADMIN"                        │
│  │  }                                       │
└──────┬───────────────────────────────────────┘
       │
       │ 200 OK + JSON
       │
       ▼
┌─────────────┐
│   Cliente   │
│  Almacena   │
│   Token en  │
│  memoria    │
└─────────────┘
```

---

## Flujo 2: Acceso a recursos (Get Productos)

```
┌─────────────┐
│   Cliente   │
│  (con token │
│ en memoria) │
└──────┬──────┘
       │
       │ GET /api/v1/productos
       │ Headers: Authorization: Bearer eyJhbGc...
       │
       ▼
┌────────────────────────────────────────────────────┐
│     PRODUCT SERVICE (Puerto 8082)                  │
│                                                    │
│  1. JwtAuthenticationFilter                       │
│     ├─ Extrae "Authorization" header              │
│     ├─ Remueve "Bearer " prefix                   │
│     └─ Token: "eyJhbGc..."                        │
│                                                   │
│  2. JwtService.extractClaims(token)               │
│     ├─ Valida firma (HMAC-SHA256)                 │
│     ├─ Valida expiración (exp claim)              │
│     └─ Retorna Claims:                            │
│         {                                         │
│           "sub": "admin",                         │
│           "rol": "ADMIN",                         │
│           "nombre": "Administrador",              │
│           "iat": 1711270000,                      │
│           "exp": 1711271800                       │
│         }                                         │
│                                                   │
│  3. SecurityConfig.authorizeHttpRequests()        │
│     ├─ Crea UsernamePasswordAuthenticationToken   │
│     ├─ Asigna autoridad: ROLE_ADMIN               │
│     └─ Guarda en SecurityContextHolder            │
│                                                   │
│  4. ProductoController.getProductos()             │
│     ├─ @Secured verificó: @Authenticated         │
│     └─ SecurityContext tiene ROLE_ADMIN           │
│         ✅ Acceso permitido                       │
│                                                   │
│  5. ProductService.listAll()                      │
│     └─ Retorna List<Producto> (en memoria)       │
│         [                                         │
│           { id:1, nombre:"Laptop HP", ... },     │
│           { id:2, nombre:"Mouse...", ... },      │
│           { id:3, nombre:"Teclado...", ... }     │
│         ]                                         │
│                                                   │
│  6. Respuesta HTTP 200 OK + JSON                 │
└──────┬──────────────────────────────────────────┘
       │
       │ [                                  
       │   {                                
       │     "id": 1,                       
       │     "nombre": "Laptop HP",         
       │     "descripcion": "Laptop...",    
       │     "precio": 850.0,               
       │     "stock": 10,                   
       │     "categoria": "Electronica"     
       │   },                               
       │   ...                              
       │ ]                                  
       │
       ▼
┌─────────────┐
│   Cliente   │
│  Procesa    │
│   JSON      │
│ (Front-end) │
└─────────────┘
```

---

## Flujo 3: Crear producto (solo ADMIN)

```
┌─────────────────────────────────────┐
│         Cliente (ROL: ADMIN)        │
│                                     │
│  POST /api/v1/productos             │
│  Authorization: Bearer token_admin  │
│  {                                  │
│    "nombre": "Monitor LG",          │
│    "descripcion": "27 pulgadas",    │
│    "precio": 350.00,                │
│    "stock": 5,                      │
│    "categoria": "Perifericos"       │
│  }                                  │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────┐
│    PRODUCT SERVICE - JwtAuthenticationFilter │
│                                             │
│  1. Valida JWT                              │
│  2. Extrae claims → rol="ADMIN"             │
│  3. Crea SecurityContext con ROLE_ADMIN     │
│                                             │
│  ✅ Continúa al controlador                 │
└──────┬──────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────┐
│    ProductoController.createProducto()       │
│                                             │
│  @PostMapping("/api/v1/productos")          │
│  @PreAuthorize("hasRole('ADMIN')")          │
│  public Producto createProducto(             │
│    @Valid @RequestBody ProductoRequest req  │
│  )                                          │
│                                             │
│  1. Valida anotaciones (jakarta.validation) │
│     ✓ nombre no está vacío                  │
│     ✓ precio >= 0                           │
│     ✓ stock >= 0                            │
│                                             │
│  2. Verifica role: hasRole('ADMIN')         │
│     ✓ SecurityContext.authorities           │
│       contiene ROLE_ADMIN                   │
│     ✅ Acceso permitido                     │
│                                             │
│  3. ProductService.create(request)          │
│     └─ Guarda en Map<Long, Producto>       │
│        (en memoria con AtomicLong ID)       │
│                                             │
│  4. Retorna objeto creado con HTTP 201      │
└──────┬──────────────────────────────────────┘
       │
       │ 201 Created
       │ {
       │   "id": 4,
       │   "nombre": "Monitor LG",
       │   "descripcion": "27 pulgadas",
       │   "precio": 350.0,
       │   "stock": 5,
       │   "categoria": "Perifericos"
       │ }
       │
       ▼
┌─────────────┐
│   Cliente   │
│  Recibe OK  │
│  Producto   │
│  creado     │
└─────────────┘
```

---

## Flujo 4: Intento de creación con rol USER (Falla)

```
┌─────────────────────────────────────┐
│         Cliente (ROL: USER)         │
│                                     │
│  POST /api/v1/productos             │
│  Authorization: Bearer token_user   │
│  {                                  │
│    "nombre": "Monitor LG",          │
│    ...                              │
│  }                                  │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────┐
│    PRODUCT SERVICE - JwtAuthenticationFilter │
│                                             │
│  1. Valida JWT                              │
│  2. Extrae claims → rol="USER"              │
│  3. Crea SecurityContext con ROLE_USER      │
│                                             │
│  ✅ Continúa al controlador                 │
└──────┬──────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────┐
│    ProductoController.createProducto()       │
│                                             │
│  @PreAuthorize("hasRole('ADMIN')")          │
│                                             │
│  1. Verifica role: hasRole('ADMIN')         │
│     ✗ SecurityContext.authorities           │
│       contiene ROLE_USER, no ROLE_ADMIN    │
│     ❌ ACCESO DENEGADO                      │
│                                             │
│  2. Problem403Handler intercepta             │
│     └─ Devuelve HTTP 403                    │
└──────┬──────────────────────────────────────┘
       │
       │ 403 Forbidden
       │ {
       │   "title": "Sin permisos",
       │   "status": 403,
       │   "detail": "No tiene permisos..."
       │ }
       │
       ▼
┌─────────────┐
│   Cliente   │
│  ERROR 403  │
│  (Sin perm) │
└─────────────┘
```

---

## Flujo 5: Request sin token (Falla)

```
┌─────────────┐
│   Cliente   │
│  (sin token)│
└──────┬──────┘
       │
       │ GET /api/v1/productos
       │ (sin header Authorization)
       │
       ▼
┌──────────────────────────────────────────────┐
│   PRODUCT SERVICE - JwtAuthenticationFilter   │
│                                              │
│  1. Busca header "Authorization"             │
│     ✗ No encontrado (null)                   │
│                                              │
│  2. Continúa al siguiente filtro/controller  │
│     pero sin SecurityContext autenticado     │
└──────┬───────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────┐
│    ProductoController.getProductos()         │
│                                              │
│  @GetMapping("/productos")                   │
│  @PreAuthorize("hasAnyRole('ADMIN','USER')") │
│                                              │
│  1. Verifica autenticación                   │
│     ✗ SecurityContext está VACIO             │
│     ❌ NECESITA AUTENTICACION                │
│                                              │
│  2. Problem401EntryPoint intercepta          │
│     └─ Devuelve HTTP 401                     │
└──────┬───────────────────────────────────────┘
       │
       │ 401 Unauthorized
       │ {
       │   "title": "No autenticado",
       │   "status": 401,
       │   "detail": "Debe enviar..."
       │ }
       │
       ▼
┌─────────────┐
│   Cliente   │
│  ERROR 401  │
│  (Sin token)│
└─────────────┘
```

---

## Tabla de decisiones de seguridad

```
┌─────────────────────────────────────────────────────────────────┐
│                      SECURITY DECISION TREE                     │
└─────────────────────────────────────────────────────────────────┘

GET /api/v1/productos
    │
    ├─ ¿Tiene header Authorization?
    │  ├─ NO → HTTP 401 Unauthorized
    │  │
    │  └─ SI → ¿Token válido?
    │     ├─ NO (expirado/corrupto) → HTTP 401
    │     │
    │     └─ SI → Extrae rol del token
    │        └─ Asigna ROLE_{rol} a SecurityContext
    │           ├─ Endpoint requiere @Authenticated
    │           │  └─ ✅ Permite tanto ADMIN como USER
    │           │
    │           └─ Endpoint requiere rol específico
    │              └─ ✓ Usuario ADMIN puede acceder
    │                 ✓ Usuario USER puede acceder

POST /api/v1/productos
    │
    ├─ ¿Tiene header Authorization?
    │  ├─ NO → HTTP 401 Unauthorized
    │  │
    │  └─ SI → ¿Token válido?
    │     ├─ NO → HTTP 401
    │     │
    │     └─ SI → Extrae rol del token
    │        ├─ rol = "ADMIN"
    │        │  └─ ✅ Permite (hasRole 'ADMIN')
    │        │
    │        └─ rol = "USER"
    │           └─ ❌ Bloquea (403 Forbidden)

DELETE /api/v1/productos/{id}
    └─ Misma lógica que POST
       Solo ADMIN puede eliminar
```

---

## Timeline de un request completo

```
[CLIENT]                   [JWT FILTER]           [CONTROLLER]       [SERVICE]
   │                            │                       │                │
   │ 1. POST login              │                       │                │
   ├──────────────────────────►  │                       │                │
   │                            │ 2. Sin JWT yet        │                │
   │                            ├──────────────────────►│                │
   │                            │                       │ 3. AuthService │
   │                            │                       ├───────────────►│
   │                            │                       │                │ 4. Genera JWT
   │                            │                       │◄───────────────┤
   │ 5. Retorna token           │                       │                │
   │◄───────────────────────────┴───────────────────────┤                │
   │                            │                       │                │
   │ 6. GET /productos + Bearer token                   │                │
   ├──────────────────────────►  │                       │                │
   │                            │ 7. Valida firma      │                │
   │                            │    & expiración      │                │
   │                            │ 8. Extrae claims     │                │
   │                            │ 9. Set SecurityCtx   │                │
   │                            ├──────────────────────►│                │
   │                            │                       │ 10. Check RBAC │
   │                            │                       │                │
   │                            │                       │ 11. ProductSvc │
   │                            │                       ├───────────────►│
   │                            │                       │                │ 12. Query
   │                            │                       │                │     data
   │                            │                       │ 13. Resultado  │
   │                            │                       │◄───────────────┤
   │ 14. 200 + JSON data        │                       │                │
   │◄──────────────────────────┴────────────────────────┤                │
   │                            │                       │                │
   
Total: ~50-100ms (red latency)
JWT processing: ~5-10ms
DB query: ~1-5ms
Serialization: ~5ms
```

---

## Conclusión

Esta arquitectura de microservicios modernos permite:

1. **Escalabilidad**: Servicios independientes, sin estado compartido
2. **Seguridad**: JWT estándar, RBAC granular
3. **Separación**: Auth y Product completamente desacoplados
4. **Testing**: Fácil de testear con MockMvc
5. **Observabilidad**: Health checks, Actuator, posibles métricas

**El flow es simple, rápido y robusto.**

