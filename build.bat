@echo off
echo Building Secure File Vault...

REM Check Java version
java -version 2>&1 | findstr /i "version" | findstr /i "22" >nul
if %errorlevel% neq 0 (
    echo Error: Java 22 is required but not found.
    echo Please install Java 22 and set JAVA_HOME correctly.
    pause
    exit /b 1
)

REM Clean and compile
mvn clean compile

REM Run the application
mvn javafx:run

echo.
echo If you see any errors, please check:
echo 1. Java 22 is installed and JAVA_HOME is set correctly
echo 2. JavaFX 24 is properly configured in pom.xml
echo 3. All dependencies are downloaded
echo.
pause 