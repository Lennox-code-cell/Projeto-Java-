@echo off
setlocal
cd /d "%~dp0"
where mvn >nul 2>nul
if errorlevel 1 (
  if exist "%USERPROFILE%\tools\apache-maven-3.10.0\bin\mvn.cmd" set "PATH=%USERPROFILE%\tools\apache-maven-3.10.0\bin;%PATH%"
  where mvn >nul 2>nul
  if errorlevel 1 (
    echo Maven 3.9 ou superior nao encontrado no PATH.
    exit /b 1
  )
)
call mvn -q clean package
if errorlevel 1 (
  echo BUILD_FALHOU
  exit /b 1
)
echo BUILD_OK
