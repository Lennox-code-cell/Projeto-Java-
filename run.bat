@echo off
setlocal
cd /d "%~dp0"
java -cp out;libs\mysql-connector-j-8.4.0.jar br.com.biblioteca.Main
