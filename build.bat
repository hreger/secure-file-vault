@echo off
echo Building Secure File Vault...

REM Clean and compile
mvn clean compile

REM Run the application
mvn exec:java -Dexec.mainClass="com.securefilevault.Main" -Dexec.args=""

echo.
echo If you see any errors, please check:
echo 1. Java 20 is installed and JAVA_HOME is set correctly
echo 2. JavaFX 24 is properly configured in pom.xml
echo 3. All dependencies are downloaded
echo.
pause 