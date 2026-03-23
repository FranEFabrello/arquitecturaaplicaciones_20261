# Modernizacion Fase 1 (No Monolitico)

Esta fase separa el sistema legacy en dos microservicios Spring Boot:

- `services/auth-service`: autenticacion y emision de JWT.
- `services/product-service`: API de productos con autorizacion por rol.

## ⚠️ Requisitos (IMPORTANTE)

- **Java JDK 17 o superior** (NO es suficiente JRE, necesitas el compilador javac).
- Maven 3.9+.

### Instalación rápida de JDK 17

Si NO tienes JDK 17:

**Opción A - Descargar desde Adoptium (recomendado):**
1. Ve a https://adoptium.net/
2. Descarga "Eclipse Temurin 17 (LTS) Latest release" → Windows x64
3. Instala en `C:\jdk-17` (o tu ubicación preferida)
4. Configura variable de entorno:
   ```powershell
   $env:JAVA_HOME = "C:\jdk-17"
   $env:Path = "$env:JAVA_HOME\bin;$env:Path"
   ```
5. Verifica: `java -version` y `javac -version`

**Opción B - Script automático (PowerShell):**
```powershell
$jdkUrl = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.11%2B9/OpenJDK17U-jdk_x64_windows_hotspot_17.0.11_9.zip"
$jdkZip = "C:\temp\jdk17.zip"
$jdkHome = "C:\jdk-17"

New-Item -ItemType Directory -Path "C:\temp" -Force | Out-Null
Invoke-WebRequest -Uri $jdkUrl -OutFile $jdkZip -UseBasicParsing
Expand-Archive -Path $jdkZip -DestinationPath $jdkHome -Force
Get-ChildItem $jdkHome | Where-Object { $_.PSIsContainer -and $_.Name -match 'jdk' } | ForEach-Object {
    Move-Item "$($_.FullName)\*" $jdkHome -Force
    Remove-Item $_.FullName -Force -ErrorAction SilentlyContinue
}
Remove-Item $jdkZip -Force
$env:JAVA_HOME = $jdkHome
$env:Path = "$jdkHome\bin;$env:Path"
java -version
```

## 📦 Arquitectura

- `auth-service` corre en `http://localhost:8081`.
- `product-service` corre en `http://localhost:8082`.
- Ambos comparten la misma clave JWT via `SECURITY_JWT_SECRET`.

## 🔌 Endpoints principales

### Auth Service

**POST /api/v1/auth/login**

Body:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response:
```json
{
  "accessToken": "eyJhbGc...",
  "tokenType": "Bearer",
  "expiresIn": 1800,
  "username": "admin",
  "nombre": "Administrador",
  "rol": "ADMIN"
}
```

### Product Service

Todos estos endpoints requieren header: `Authorization: Bearer {accessToken}`

- `GET /api/v1/productos` → Lista todos (roles: USER, ADMIN)
- `GET /api/v1/productos/{id}` → Detalle (roles: USER, ADMIN)
- `POST /api/v1/productos` → Crear (solo ADMIN)
- `PUT /api/v1/productos/{id}` → Actualizar (solo ADMIN)
- `DELETE /api/v1/productos/{id}` → Eliminar (solo ADMIN)

### Health Checks

- `GET http://localhost:8081/actuator/health` → Auth service
- `GET http://localhost:8082/actuator/health` → Product service

## ⚙️ Compilación

```powershell
cd "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1"

# Compilar ambos servicios
mvn clean package -DskipTests
```

Esto generará:
- `services/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar`
- `services/product-service/target/product-service-0.0.1-SNAPSHOT.jar`

## ▶️ Ejecución

**Terminal 1 - Auth Service:**
```powershell
cd "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1\services\auth-service"
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

**Terminal 2 - Product Service:**
```powershell
cd "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1\services\product-service"
java -jar target/product-service-0.0.1-SNAPSHOT.jar
```

Deberías ver:
```
... Started AuthServiceApplication in X seconds
... Started ProductServiceApplication in X seconds
```

## 🧪 Pruebas rápidas

### 1. Login (obtener token)

```powershell
$response = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8081/api/v1/auth/login" `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"admin123"}'

$token = $response.accessToken
Write-Output "Token: $token"
```

### 2. Listar productos (con token)

```powershell
Invoke-RestMethod -Method Get `
  -Uri "http://localhost:8082/api/v1/productos" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json | Write-Output
```

### 3. Crear producto (solo ADMIN)

```powershell
$newProduct = @{
    nombre = "Monitor LG 27 pulgadas"
    descripcion = "Monitor UHD 4K"
    precio = 350.00
    stock = 5
    categoria = "Perifericos"
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8082/api/v1/productos" `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType "application/json" `
  -Body $newProduct | ConvertTo-Json | Write-Output
```

### 4. Intentar crear con rol USER (debe fallar con 403)

```powershell
$userResponse = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8081/api/v1/auth/login" `
  -ContentType "application/json" `
  -Body '{"username":"usuario","password":"user123"}'

$userToken = $userResponse.accessToken

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8082/api/v1/productos" `
  -Headers @{ Authorization = "Bearer $userToken" } `
  -ContentType "application/json" `
  -Body $newProduct -ErrorAction SilentlyContinue
```

## 🧬 Credenciales de prueba

| Username | Password | Rol   | Permisos                      |
|----------|----------|-------|-------------------------------|
| admin    | admin123 | ADMIN | Leer, crear, editar, eliminar |
| usuario  | user123  | USER  | Solo lectura                  |

## 📝 Ejecutar tests

```powershell
cd "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1"
mvn test
```

Tests verifican:
- ✓ Login con credenciales válidas retorna `200` + token
- ✓ Login con credenciales inválidas retorna `401`
- ✓ GET /productos sin token retorna `401`
- ✓ POST /productos con rol `USER` retorna `403`
- ✓ POST /productos con rol `ADMIN` retorna `201`
- ✓ Health checks responden `UP`

## 🐳 Docker (Alternativa)

Si tienes Docker Desktop corriendo:

```powershell
cd "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1"
docker compose up --build
```

## 📦 Estructura del proyecto

```
modernizacion-fase1/
├── README.md                    (este archivo)
├── pom.xml                      (agregador Maven)
├── docker-compose.yml
├── docs/
│   └── entregable1-aceptacion.md
└── services/
    ├── auth-service/
    │   ├── pom.xml
    │   ├── src/main/java/
    │   │   └── com/uade/modernizacion/auth/
    │   │       ├── AuthServiceApplication.java
    │   │       ├── api/
    │   │       │   ├── AuthController.java
    │   │       │   ├── ApiExceptionHandler.java
    │   │       │   └── dto/
    │   │       │       ├── LoginRequest.java
    │   │       │       └── LoginResponse.java
    │   │       ├── domain/
    │   │       │   └── Usuario.java
    │   │       ├── security/
    │   │       │   └── JwtService.java
    │   │       └── service/
    │   │           ├── AuthService.java
    │   │           └── UserService.java
    │   └── src/test/java/
    │       └── com/uade/modernizacion/auth/
    │           ├── AuthControllerTest.java
    │           └── AuthHealthTest.java
    └── product-service/
        ├── pom.xml
        ├── src/main/java/
        │   └── com/uade/modernizacion/product/
        │       ├── ProductServiceApplication.java
        │       ├── api/
        │       │   ├── ProductoController.java
        │       │   ├── ApiExceptionHandler.java
        │       │   └── dto/
        │       │       └── ProductoRequest.java
        │       ├── domain/
        │       │   └── Producto.java
        │       ├── security/
        │       │   ├── JwtService.java
        │       │   ├── JwtAuthenticationFilter.java
        │       │   ├── Problem401EntryPoint.java
        │       │   ├── Problem403Handler.java
        │       │   └── SecurityConfig.java
        │       └── service/
        │           ├── ProductService.java
        │           └── ProductNotFoundException.java
        └── src/test/java/
            └── com/uade/modernizacion/product/
                ├── ProductoSecurityTest.java
                └── ProductHealthTest.java
```

## 🚀 Próximos pasos

Después de validar esta fase, la siguiente sería:

- **Fase 2**: Persistencia real (PostgreSQL + Flyway)
- **Fase 3**: API Gateway (Spring Cloud Gateway)
- **Fase 4**: Frontend moderno (React/Vue consumiendo APIs)
- **Fase 5**: Pipeline CI/CD (GitHub Actions / GitLab CI)
- **Fase 6**: Observabilidad (Prometheus, Grafana, ELK)

## 📞 Troubleshooting

### Error: "No compiler is provided in this environment"
→ Instala JDK 17 (no solo JRE). Ver sección "Requisitos".

### Error: "Connection refused" en localhost:8082
→ Verifica que `product-service` esté ejecutándose en la terminal 2.

### Error: "Credenciales invalidas" en login
→ Verifica usuario/password correctos: `admin`/`admin123` o `usuario`/`user123`.

### Error: "Invalid token" en requests a product-service
→ El token expiró (30 min). Vuelve a hacer login para obtener uno nuevo.

