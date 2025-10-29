@echo off
echo Starting Healthy Recipe Platform Server...
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Maven is not found in PATH. Please ensure Maven is installed and added to PATH.
    echo.
    pause
    exit /b 1
)

REM Check if Java is available
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo Java is not found in PATH. Please ensure Java 17+ is installed and added to PATH.
    echo.
    pause
    exit /b 1
)

REM Set JAVA_HOME if not set
if "%JAVA_HOME%"=="" (
    echo Warning: JAVA_HOME is not set. Using java from PATH.
)

echo Cleaning and compiling the project...
call mvn clean compile -q

if %errorlevel% neq 0 (
    echo Build failed. Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Starting Spring Boot application...
echo The server will run on http://localhost:8080
echo.
echo To stop the server, close this window or press Ctrl+C
echo.

REM Start the application
start "Healthy Recipe Server" /min cmd /c "mvn spring-boot:run"

echo Server is starting in background...
echo You can now close this window and access the application at:
echo http://localhost:8080
echo.

timeout /t 5 >nul

REM Open the application in default browser
start http://localhost:8080

exit /b 0