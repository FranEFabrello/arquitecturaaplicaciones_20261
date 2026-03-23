@echo off
REM Script para ejecutar product-service (Windows)

setlocal

cd /d "C:\Users\franc\Desktop\_Organizado\Proyectos\arquitecturaaplicaciones_20261\modernizacion-fase1\services\product-service"

echo ================================================
echo Iniciando PRODUCT-SERVICE en puerto 8082
echo ================================================
echo.
echo URL: http://localhost:8082
echo.

java -jar target\*.jar

