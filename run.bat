@echo off
setlocal EnableDelayedExpansion

REM Set the module path to the lib directory
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
     -Djavafx.verbose=true ^
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

pause 