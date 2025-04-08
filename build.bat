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
echo Cleaning and compiling...
mvn clean compile

REM Copy dependencies
echo Copying dependencies...
mvn dependency:copy-dependencies -DoutputDirectory=target/lib

REM Verify JavaFX modules
echo Checking JavaFX modules...
dir target\lib\javafx-*.jar

REM Set the module path
set "MODULE_PATH=%CD%\target\lib"

echo Starting application...
echo The application window should appear on your screen.

REM Run the application with verbose module loading
java --module-path "%MODULE_PATH%" ^
     --add-modules javafx.controls,javafx.fxml,javafx.graphics ^
     -cp target\classes ^
     --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED ^
     --add-opens javafx.controls/javafx.scene.control=ALL-UNNAMED ^
     --add-opens javafx.base/com.sun.javafx.runtime=ALL-UNNAMED ^
     com.securefilevault.Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Application failed to start. Please check the following:
    echo 1. JavaFX modules are present in target\lib directory
    echo 2. No other JavaFX applications are running
    echo 3. Your system supports JavaFX (Windows, Linux, or macOS)
    echo.
    echo Error code: %ERRORLEVEL%
)

echo.
echo If you see any errors, please check:
echo 1. Java 22 or higher is installed and JAVA_HOME is set correctly
echo 2. JavaFX 24 is properly configured in pom.xml
echo 3. All dependencies are downloaded
echo.
pause 