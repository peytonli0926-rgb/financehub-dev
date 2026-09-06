@echo off
setlocal
cd /d "%~dp0"
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0start-financehub.ps1"
if errorlevel 1 (
  echo.
  echo FinanceHub startup failed. Check the messages and runtime-logs folder.
  pause
  exit /b 1
)
echo.
echo FinanceHub startup completed. You may close this window.
pause

