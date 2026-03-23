# Sistema Legacy UADE

Este es un proyecto monolítico basado en Java EE (Servlets, JSP, JDBC) para la gestión de productos, utilizando Maven y el plugin de Tomcat 7.

## 🚀 Requisitos previos

*   **Java JDK 11** (o superior).
*   **Apache Maven** instalado (o usar el `mvnw` incluido).
*   **IntelliJ IDEA** (Recomendado).

## 🛠️ Cómo ejecutar el proyecto

### Opción 1: Desde la Terminal (Recomendado)
Abre una terminal en la raíz del proyecto y ejecuta:
```bash
mvn tomcat7:run
```
Una vez que el servidor esté activo, accede a: **`http://localhost:8080/legacy`**

### Opción 2: Desde IntelliJ IDEA
1. En el lateral derecho, abre la pestaña **Maven**.
2. Despliega la carpeta `legacy` -> `Plugins` -> `tomcat7`.
3. Haz doble clic en el comando **`tomcat7:run`**.

### Opción 3: Configurar el botón "Play"
1. Haz clic en **Add Configuration...** arriba a la derecha.
2. Haz clic en el botón **+** y selecciona **Maven**.
3. En **Name** escribe: `Ejecutar Legacy`.
4. En **Command line** escribe: `tomcat7:run`.
5. Haz clic en **OK** y ahora podrás usar el botón verde de "Play".

## 🔐 Credenciales de Acceso

El sistema utiliza una base de datos H2 en memoria que se reinicia con cada ejecución. Los usuarios por defecto son:

| Usuario | Password | Rol |
| :--- | :--- | :--- |
| `admin` | `admin123` | **Administrador** (Puede crear/editar productos) |
| `usuario` | `user123` | **Usuario estándar** (Solo lectura) |

## 📁 Estructura del Proyecto
*   **`src/main/java`**: Lógica de negocio, servlets, DAOs y modelos.
*   **`src/main/webapp`**: Vistas JSP y archivos estáticos (CSS).
*   **`src/main/webapp/WEB-INF/web.xml`**: Descriptor de despliegue del servidor.
*   **`pom.xml`**: Configuración de Maven y dependencias.
