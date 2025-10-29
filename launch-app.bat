@echo off
echo Starting Healthy Recipe Platform...

REM Start the server in background
start /min "" "%~dp0run-server-visible.bat"

REM Wait a moment for server to start
echo Waiting for server to initialize...
timeout /t 10 /nobreak >nul

REM Open the application in browser
echo Opening application in browser...
start http://localhost:8080

echo.
echo Application is starting at: http://localhost:8080
echo.
echo To stop the server, run stop-server.bat or close the server window.
echo.
pause