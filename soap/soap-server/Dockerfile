# ── Etapa 1: Build ──────────────────────────────────────────────────────────
# Usa la imagen oficial de Maven con Java 21 para compilar el proyecto.
# El plugin jaxb2-maven-plugin se ejecuta aquí y genera las clases desde el XSD.
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos el pom.xml primero para aprovechar la caché de layers de Docker.
# Si el pom.xml no cambia, Maven no re-descargará las dependencias.
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Etapa 2: Runtime ─────────────────────────────────────────────────────────
# Imagen más liviana, solo tiene el JRE (sin Maven ni código fuente).
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/soap-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
