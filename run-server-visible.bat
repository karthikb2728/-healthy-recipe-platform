@echo off
title Healthy Recipe Platform Server

REM Change to the project directory
cd /d "%~dp0"

echo ========================================
echo  Healthy Recipe Platform Server
echo ========================================
echo.

REM Check for Maven
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Maven not found. Please install Maven and add to PATH.
    echo.
    pause
    exit /b 1
)

REM Check for Java
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java not found. Please install Java 17+ and add to PATH.
    echo.
    pause
    exit /b 1
)

echo [INFO] Java and Maven found successfully.
echo [INFO] Starting server preparation...
echo.

REM Clean and compile the project quietly
echo [INFO] Cleaning previous build...
call mvn clean -q

echo [INFO] Compiling project...
call mvn compile -q

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed. Check the error messages above.
    pause
    exit /b 1
)

echo [INFO] Compilation successful.
echo.
echo [INFO] Starting Healthy Recipe Platform Server...
echo [INFO] Server URL: http://localhost:8080
echo [INFO] API Documentation: http://localhost:8080/swagger-ui.html
echo.
echo [INFO] Press Ctrl+C to stop the server
echo ========================================
echo.

REM Start the Spring Boot application
call mvn spring-boot:run

pause