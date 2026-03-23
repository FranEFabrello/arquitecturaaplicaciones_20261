@echo off
REM Script para ejecutar auth-service (Windows)

setlocal

cd /d "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1\services\auth-service"

echo ================================================
echo Iniciando AUTH-SERVICE en puerto 8081
echo ================================================
echo.
echo URL: http://localhost:8081
echo.

java -jar target\*.jar

