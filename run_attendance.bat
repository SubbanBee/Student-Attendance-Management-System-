@echo off
REM ====== Attendance System - College ERP ======
set PROJECT_DIR=%~dp0
set MYSQL_CONNECTOR=C:\Users\subba\Downloads\mysql-connector-j-9.6.0.jar

cd /d %PROJECT_DIR%

REM ====== Check MySQL connector JAR ======
if not exist "%MYSQL_CONNECTOR%" (
    echo.
    echo ERROR: MySQL connector JAR not found.
    echo Edit MYSQL_CONNECTOR in this file to your mysql-connector-j-*.jar path.
    echo Current: %MYSQL_CONNECTOR%
    pause
    exit /b 1
)

REM ====== Compile ======
echo Compiling...
javac -encoding UTF-8 *.java 2>nul
if ERRORLEVEL 1 (
    javac *.java
    if ERRORLEVEL 1 (
        echo Compilation failed.
        pause
        exit /b 1
    )
)

REM ====== Run ======
echo Starting Attendance System...
java -cp ".;%MYSQL_CONNECTOR%" AcademicSystem

pause
