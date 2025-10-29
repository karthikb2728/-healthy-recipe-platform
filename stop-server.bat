@echo off
echo Stopping Healthy Recipe Platform Server...

REM Find and kill Java processes running Spring Boot
for /f "tokens=2" %%i in ('tasklist /fi "imagename eq java.exe" ^| find "java.exe"') do (
    echo Checking process %%i...
    tasklist /fi "pid eq %%i" | find "spring-boot" >nul
    if not errorlevel 1 (
        echo Stopping Spring Boot server (PID: %%i)...
        taskkill /pid %%i /f
    )
)

REM Alternative: Kill all Maven processes
taskkill /im "mvn.cmd" /f 2>nul
taskkill /im "mvn" /f 2>nul

REM Kill Java processes that might be running Spring Boot
wmic process where "CommandLine like '%%spring-boot%%'" delete 2>nul

echo.
echo Server stopped successfully.
echo.
pause