@echo off
setlocal EnableDelayedExpansion
echo Building Secure File Vault...

REM Check Java version
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set "JAVA_VERSION=%%g"
    set "JAVA_VERSION=!JAVA_VERSION:"=!"
    goto :version_check
)

:version_check
echo Java version: %JAVA_VERSION%
for /f "tokens=1 delims=." %%a in ("%JAVA_VERSION%") do set "MAJOR_VERSION=%%a"
if %MAJOR_VERSION% GEQ 22 (
    echo Java version is compatible
) else (
    echo Error: Java 22 or higher is required.
    echo Current version: %JAVA_VERSION%
    echo Please install Java 22 or higher and set JAVA_HOME correctly.
    pause
    exit /b 1
)

REM Clean and compile
mvn clean compile

REM Run the application
mvn exec:java -Dexec.mainClass="com.securefilevault.Main" -Dexec.args="--add-modules javafx.controls,javafx.fxml"

echo.
echo If you see any errors, please check:
echo 1. Java 22 or higher is installed and JAVA_HOME is set correctly
echo 2. JavaFX 24 is properly configured in pom.xml
echo 3. All dependencies are downloaded
echo.
pause 