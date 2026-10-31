@echo off
setlocal
cd /d "c:\Users\User\Documents\poo"
mkdir libs 2>nul
powershell.exe -NoLogo -NoProfile -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar' -OutFile 'c:\Users\User\Documents\poo\libs\mysql-connector-j-8.4.0.jar'"
del /f /q sources.txt 2>nul
for /r "%CD%\src\main\java" %%f in (*.java) do (
  echo %%~ff>> sources.txt
)
if not exist sources.txt (
  echo COMPILACAO_FALHOU
  exit /b 1
)
javac --release 25 -cp "libs\mysql-connector-j-8.4.0.jar" -d out @sources.txt
if errorlevel 1 (
  echo COMPILACAO_FALHOU
  exit /b 1
)
echo COMPILACAO_OK
