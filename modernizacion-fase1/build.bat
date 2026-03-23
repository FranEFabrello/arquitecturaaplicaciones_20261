@echo off
REM Script para compilar los microservicios (Windows)

setlocal enabledelayedexpansion

echo ================================================
echo Modernizacion Fase 1 - Compilacion y Ejecucion
echo ================================================
echo.

REM Verificar Java
echo Verificando Java...
java -version 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java no se encuentra instalado o JAVA_HOME no está configurado.
    exit /b 1
)

echo.
echo Compilando auth-service...
cd /d "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1\services\auth-service"
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Compilacion de auth-service fallo.
    exit /b 1
)

echo.
echo Compilando product-service...
cd /d "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1\services\product-service"
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Compilacion de product-service fallo.
    exit /b 1
)

echo.
echo ================================================
echo Compilacion Exitosa!
echo ================================================
echo.
echo Para ejecutar auth-service en terminal 1:
echo   cd services\auth-service
echo   java -jar target\*.jar
echo.
echo Para ejecutar product-service en terminal 2:
echo   cd services\product-service
echo   java -jar target\*.jar
echo.
echo Luego acceder a:
echo   http://localhost:8081/actuator/health (auth-service)
echo   http://localhost:8082/actuator/health (product-service)
echo.
pause

