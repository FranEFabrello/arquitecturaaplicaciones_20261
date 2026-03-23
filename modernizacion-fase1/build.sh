#!/bin/bash
# Script para instalar JDK 17 y ejecutar los microservicios

set -e

# 1. Detectar sistema operativo y arquitectura
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
    echo "Windows detectado. Descargando Eclipse Temurin JDK 17..."

    # Descargar JDK 17 (versión portable)
    DOWNLOAD_URL="https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.11%2B9/OpenJDK17U-jdk_x64_windows_hotspot_17.0.11_9.zip"
    JDK_ZIP="/tmp/jdk17.zip"
    JDK_HOME="/opt/jdk-17"

    if [ ! -d "$JDK_HOME" ]; then
        echo "Descargando JDK 17..."
        curl -L "$DOWNLOAD_URL" -o "$JDK_ZIP"
        mkdir -p "$JDK_HOME"
        unzip -q "$JDK_ZIP" -d "$JDK_HOME"
        rm "$JDK_ZIP"
    fi

    export JAVA_HOME="$JDK_HOME"
    export PATH="$JAVA_HOME/bin:$PATH"
else
    echo "Sistema Linux/Mac detectado. Usando Java del sistema..."
fi

# 2. Verificar Java
echo "Verificando Java..."
java -version
javac -version

# 3. Compilar proyectos
echo ""
echo "Compilando auth-service..."
cd "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1\services\auth-service"
mvn clean package -DskipTests

echo ""
echo "Compilando product-service..."
cd "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1\services\product-service"
mvn clean package -DskipTests

echo ""
echo "✓ Compilacion exitosa. Servicios listos para ejecutar."
echo ""
echo "Para ejecutar auth-service:"
echo "  cd services/auth-service && java -jar target/*.jar"
echo ""
echo "Para ejecutar product-service:"
echo "  cd services/product-service && java -jar target/*.jar"

