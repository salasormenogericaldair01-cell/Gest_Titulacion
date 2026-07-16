@echo off
title IESTP SUIZA - Sistema de Registro de Proyectos de Titulacion
echo ===============================================================================
echo            IESTP SUIZA - SISTEMA DE TITULACION (MODULO EJECUTABLE)
echo ===============================================================================
echo Iniciando aplicacion...
echo.

cd /d "%~dp0"

IF EXIST "dist\Gestion_Titulacion_Ejecutable.jar" (
    echo [OK] Ejecutando JAR unificado standalone...
    start "" javaw -jar "dist\Gestion_Titulacion_Ejecutable.jar"
    goto :fin
)

IF EXIST "dist\Gestion_Titulacion.jar" (
    echo [OK] Ejecutando JAR principal desde carpeta dist...
    start "" javaw -jar "dist\Gestion_Titulacion.jar"
    goto :fin
)

echo [AVISO] No se encontraron los archivos JAR compilados en dist/
echo Por favor, abra el proyecto en NetBeans y presione "Clean & Build" (Limpiar y Construir)
echo para generar el ejecutable unico Gestion_Titulacion_Ejecutable.jar.
pause
exit /b

:fin
exit /b
