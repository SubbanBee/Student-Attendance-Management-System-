@echo off
echo ==========================================
echo College ERP Attendance System Compiler
echo ==========================================

REM Set up classpath for all JARs
set CLASSPATH=.;mysql-connector-j-9.6.0.jar;lib\flatlaf-3.4.1.jar

echo [1] Compiling Java source files...
javac -cp "%CLASSPATH%" src\com\college\*.java
if %ERRORLEVEL% neq 0 (
    echo Compilation failed! Please check your code.
    pause
    exit /b %ERRORLEVEL%
)
echo Compilation successful.

echo [2] Launching College ERP...
java -cp "src;%CLASSPATH%" com.college.CollegeERP

pause
