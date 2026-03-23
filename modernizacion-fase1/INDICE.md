# 📚 Índice de documentación - Modernizacion Fase 1

Bienvenido a la **Modernización Fase 1** del sistema legacy. Este documento te guía por toda la documentación disponible.

---

## 🎯 Para empezar rápidamente

1. **[QUICKSTART.md](QUICKSTART.md)** ⭐⭐⭐  
   _5 minutos para tener todo ejecutando_
   - Instalación de JDK 17 (paso a paso)
   - Compilación del proyecto
   - Ejecución de servicios
   - Validación rápida

2. **[README.md](README.md)**  
   _Guía completa de referencia_
   - Requisitos y setup
   - Endpoints disponibles
   - Credenciales de prueba
   - Ejemplos con PowerShell
   - Troubleshooting

---

## 📖 Para entender la arquitectura

3. **[ARQUITECTURA.md](ARQUITECTURA.md)**  
   _Comparación legacy vs moderna_
   - Diagramas de arquitectura
   - Comparativa de tecnologías
   - Ventajas de la modernización
   - Estrategia de seguridad (Session vs JWT)
   - Diagrama de deployment

4. **[FLUJOS.md](FLUJOS.md)**  
   _Flujos de ejecución con diagramas ASCII_
   - Flujo de login (obtener JWT)
   - Flujo de acceso a recursos
   - Flujo de creación de datos
   - Casos de error (401, 403)
   - Timeline de un request completo

---

## ✅ Para validar que todo funciona

5. **[docs/entregable1-aceptacion.md](docs/entregable1-aceptacion.md)**  
   _Criterios de aceptación (checklist)_
   - ✓ Autenticación
   - ✓ Autorización
   - ✓ Health checks
   - Todos los tests

---

## 🗂️ Estructura del código

```
modernizacion-fase1/
│
├── 📄 README.md                  ← Guía principal
├── 📄 QUICKSTART.md             ← Para empezar YA
├── 📄 ARQUITECTURA.md           ← Entender el diseño
├── 📄 FLUJOS.md                 ← Cómo funcionan las requests
├── 📄 INDICE.md                 ← Este archivo
│
├── pom.xml                      ← Agregador Maven
├── docker-compose.yml           ← Orquestación contenedores
│
├── services/
│   │
│   ├── auth-service/            ← Autenticación JWT
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   ├── src/main/java/
│   │   │   └── com/uade/modernizacion/auth/
│   │   │       ├── AuthServiceApplication.java
│   │   │       ├── api/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── ApiExceptionHandler.java
│   │   │       │   └── dto/
│   │   │       │       ├── LoginRequest.java
│   │   │       │       └── LoginResponse.java
│   │   │       ├── domain/
│   │   │       │   └── Usuario.java
│   │   │       ├── security/
│   │   │       │   └── JwtService.java
│   │   │       └── service/
│   │   │           ├── AuthService.java
│   │   │           └── UserService.java
│   │   ├── src/test/java/
│   │   │   └── AuthControllerTest.java
│   │   │   └── AuthHealthTest.java
│   │   └── src/main/resources/
│   │       └── application.properties
│   │
│   └── product-service/         ← CRUD productos
│       ├── pom.xml
│       ├── Dockerfile
│       ├── src/main/java/
│       │   └── com/uade/modernizacion/product/
│       │       ├── ProductServiceApplication.java
│       │       ├── api/
│       │       │   ├── ProductoController.java
│       │       │   ├── ApiExceptionHandler.java
│       │       │   └── dto/
│       │       │       └── ProductoRequest.java
│       │       ├── domain/
│       │       │   └── Producto.java
│       │       ├── security/
│       │       │   ├── JwtService.java
│       │       │   ├── JwtAuthenticationFilter.java
│       │       │   ├── Problem401EntryPoint.java
│       │       │   ├── Problem403Handler.java
│       │       │   └── SecurityConfig.java
│       │       └── service/
│       │           ├── ProductService.java
│       │           └── ProductNotFoundException.java
│       ├── src/test/java/
│       │   ├── ProductoSecurityTest.java
│       │   └── ProductHealthTest.java
│       └── src/main/resources/
│           └── application.properties
│
└── docs/
    ├── entregable1-aceptacion.md ← Checklist de aceptación
    └── ... (más docs en futuras fases)
```

---

## 🚀 Roadmap por tema

### Configuración e instalación
- Necesitas JDK 17? → **QUICKSTART.md** (Sección "Instalar JDK 17")
- Necesitas compilar? → **QUICKSTART.md** (Sección "Compilación")
- Problemas al instalar? → **README.md** (Sección "Troubleshooting")

### Ejecución
- Cómo levantar los servicios? → **QUICKSTART.md** (Sección "Ejecución")
- Cómo usar Docker Compose? → **README.md** (Sección "Docker")

### Probar la API
- Ejemplos con PowerShell? → **README.md** (Sección "Pruebas rápidas")
- Curls para bash/Linux? → **QUICKSTART.md** (Nota: usar curl en lugar de PowerShell)

### Entender el diseño
- Por qué microservicios? → **ARQUITECTURA.md**
- Cómo se autentica? → **FLUJOS.md** (Flujo 1: Autenticación)
- Cómo se autoriza? → **FLUJOS.md** (Flujo 3: Control de acceso)
- Qué pasa con un token inválido? → **FLUJOS.md** (Flujo 5: Sin token)

### Validar que todo funciona
- Checklist completo? → **docs/entregable1-aceptacion.md**
- Tests? → Ejecutar `mvn test` en raíz

### Código
- Quiero entender JwtService? → `services/auth-service/src/main/java/.../security/JwtService.java`
- Quiero entender seguridad? → `services/product-service/src/main/java/.../security/SecurityConfig.java`
- Quiero ver un test? → `services/product-service/src/test/java/.../ProductoSecurityTest.java`

---

## 🎓 Learning path recomendado

Si eres **nuevo en esta arquitectura**:

1. Lee **QUICKSTART.md** → Entiende qué se va a hacer
2. Instala JDK 17 → Sigue los pasos
3. Compila y ejecuta → Levanta los servicios
4. Prueba los endpoints → Usa los ejemplos
5. Lee **ARQUITECTURA.md** → Entiende por qué está diseñado así
6. Lee **FLUJOS.md** → Entiende cómo funciona internamente
7. Explora el código → Abre los fuentes y sigue los flujos

Si ya tienes **experiencia con microservicios**:

1. Salta directo a **QUICKSTART.md** (sección Compilación)
2. Lee **ARQUITECTURA.md** rápidamente
3. Explora el código directamente
4. Ejecuta los tests

Si necesitas **context histórico**:

1. Lee el README.md del `legacy-monolito/` (carpeta padre)
2. Lee **ARQUITECTURA.md** (sección "Legacy: Session-based")
3. Compara con **ARQUITECTURA.md** (sección "Modernización: JWT-based")

---

## 💡 Tips útiles

### Terminal
- Usa **PowerShell** en Windows (mejor que cmd.exe)
- Usa **bash** en Linux/Mac
- Ten **2 terminales** abiertas: una para auth-service, otra para product-service

### Editor
- Recomendado: **IntelliJ IDEA Community** (gratuito)
- También funciona: VS Code + Extension Pack for Java
- Solo lectura: Cualquier editor de texto

### Git
- (Opcional) Clona/forkea este repositorio
- Branch principal: `main`
- Futuras fases irán en branches separados

### CI/CD
- Fase 1: Manual (ejecutar `mvn test` y `java -jar`)
- Fase 2: Docker + Docker Compose
- Fase 3: GitHub Actions / GitLab CI

---

## 📞 FAQ rápido

**P: ¿Por qué dos servicios y no uno?**  
R: Separación de responsabilidades y escalabilidad independiente. Ver **ARQUITECTURA.md**.

**P: ¿Los datos se pierden al reiniciar?**  
R: Sí, porque ahora usamos H2 en memoria. Fase 2 agregará PostgreSQL persistente.

**P: ¿Puedo usar esto en producción?**  
R: Como MVP/PoC sí. Para producción agregar: BD real, API Gateway, logging centralizado, monitoring.

**P: ¿Cómo agrego más usuarios?**  
R: Edita `UserService.java` (add usuario en constructor). Fase 2 tendrá DB.

**P: ¿Cómo cambio el puerto?**  
R: En `application.properties` de cada servicio. Ej: `server.port=9081`

**P: ¿Necesito Docker para ejecutar?**  
R: No, puede ser con JDK + Maven local. Docker es opcional.

**P: ¿Es seguro usar esto en producción?**  
R: No aún. Falta: HTTPS, refresh tokens, auditaría, rate-limiting.

**P: ¿Qué sigue después de Fase 1?**  
R: Ver **QUICKSTART.md** (sección "Próximos pasos") o esta documentación (arriba, vision Fase 2).

---

## 📊 Documentación por archivo

| Archivo | Tipo | Propósito | Lectura |
|---------|------|----------|---------|
| **README.md** | Guía | Referencia completa | 20 min |
| **QUICKSTART.md** | Guía | Ejecución rápida | 5 min |
| **ARQUITECTURA.md** | Análisis | Decisiones de diseño | 15 min |
| **FLUJOS.md** | Diagramas | Cómo funciona internamente | 10 min |
| **docs/entregable1-aceptacion.md** | Checklist | Validación | 2 min |
| **INDICE.md** (este) | Navegación | Encontrar info | 5 min |

---

## 🔗 Links útiles

### Referencia
- [Spring Boot 3.2 Docs](https://spring.io/projects/spring-boot)
- [Spring Security Docs](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/) - Debuguear tokens
- [REST API Best Practices](https://restfulapi.net/)

### Herramientas
- [Postman](https://www.postman.com/) - Testing de APIs
- [Insomnia](https://insomnia.rest/) - Alternativa a Postman
- [jwt-cli](https://github.com/mike-engel/jwt-cli) - Decodificar JWT en terminal

### Comunidad
- [Spring Community](https://spring.io/community)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/spring-boot)

---

## ✅ Checklist de lectura recomendado

Marca lo que leíste:

- [ ] QUICKSTART.md
- [ ] README.md
- [ ] ARQUITECTURA.md
- [ ] FLUJOS.md
- [ ] docs/entregable1-aceptacion.md
- [ ] Miré el código de AuthController
- [ ] Miré el código de SecurityConfig
- [ ] Ejecuté los servicios
- [ ] Hice un login con curl/PowerShell
- [ ] Obtuve un producto
- [ ] Intenté violar un permiso (USER POST) y recibí 403

---

## 🎯 Resumen de una línea

**Modernizacion Fase 1 = Auth Service (JWT) + Product Service (REST con RBAC).**

---

## 📝 Notas finales

Esta documentación está viva. Si encuentras:
- Errores → Corrige en los archivos
- Ambigüedades → Aclara en los archivos
- Mejoras → Agrega ejemplos y explicaciones

**¡Bienvenido a los microservicios modernos!** 🚀

Para empezar: abre **QUICKSTART.md** ahora.

