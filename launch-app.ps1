# Healthy Recipe Platform Server Launcher
# PowerShell version for better cross-platform compatibility

Write-Host "========================================" -ForegroundColor Green
Write-Host "  Healthy Recipe Platform Server" -ForegroundColor Green  
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Change to script directory
Set-Location $PSScriptRoot

# Check for Maven
try {
    $mvnVersion = mvn --version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] Maven found" -ForegroundColor Green
    } else {
        throw "Maven not found"
    }
} catch {
    Write-Host "[ERROR] Maven not found. Please install Maven and add to PATH." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Check for Java
try {
    $javaVersion = java -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] Java found" -ForegroundColor Green
    } else {
        throw "Java not found"
    }
} catch {
    Write-Host "[ERROR] Java not found. Please install Java 17+ and add to PATH." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "[INFO] Preparing server..." -ForegroundColor Cyan

# Clean and compile
Write-Host "[INFO] Cleaning previous build..." -ForegroundColor Yellow
& mvn clean -q

Write-Host "[INFO] Compiling project..." -ForegroundColor Yellow  
& mvn compile -q

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[OK] Compilation successful" -ForegroundColor Green
Write-Host ""

# Start server in background
Write-Host "[INFO] Starting server in background..." -ForegroundColor Cyan
Write-Host "[INFO] Server URL: http://localhost:8080" -ForegroundColor Yellow
Write-Host "[INFO] API Docs: http://localhost:8080/swagger-ui.html" -ForegroundColor Yellow
Write-Host ""

# Start Spring Boot application in a new window
$serverProcess = Start-Process powershell -ArgumentList "-Command", "mvn spring-boot:run" -WindowStyle Minimized -PassThru

Write-Host "[INFO] Server starting... (PID: $($serverProcess.Id))" -ForegroundColor Green
Write-Host "[INFO] Waiting for initialization..." -ForegroundColor Yellow

# Wait for server to start
Start-Sleep -Seconds 8

# Open browser
Write-Host "[INFO] Opening application in browser..." -ForegroundColor Cyan
Start-Process "http://localhost:8080"

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Application is running!" -ForegroundColor Green
Write-Host "URL: http://localhost:8080" -ForegroundColor Yellow
Write-Host "Server PID: $($serverProcess.Id)" -ForegroundColor Yellow
Write-Host ""
Write-Host "To stop the server:" -ForegroundColor Cyan
Write-Host "1. Run stop-server.bat" -ForegroundColor White
Write-Host "2. Or kill process ID: $($serverProcess.Id)" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Green

Read-Host "Press Enter to exit launcher (server will continue running)"