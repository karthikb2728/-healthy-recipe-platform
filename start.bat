@echo off
echo Starting Healthy Recipe Platform Setup...

:: Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java is not installed. Please install Java 17 or later.
    pause
    exit /b 1
)

:: Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven is not installed. Please install Maven 3.6 or later.
    pause
    exit /b 1
)

echo Building application...
mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo Starting application...
java -jar target/healthy-recipe-platform-1.0.0.jar

echo Application started successfully!
echo Access the application at: http://localhost:8080
echo API Documentation: http://localhost:8080/swagger-ui.html
pause