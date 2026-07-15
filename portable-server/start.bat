@echo off
setlocal

REM Run from this folder so relative paths like data/*.txt work
set "APP_HOME=%~dp0"
cd /d "%APP_HOME%"

if not exist "sut-shabu.war" (
  echo [ERROR] sut-shabu.war not found in %APP_HOME%
  echo Copy target\sut-shabu.war here first.
  pause
  exit /b 1
)

if not exist "data" (
  echo [ERROR] data folder not found in %APP_HOME%
  echo Copy the project's data\ folder here first.
  pause
  exit /b 1
)

if not exist "jetty-runner.jar" (
  echo [ERROR] jetty-runner.jar not found in %APP_HOME%
  echo Download jetty-runner.jar and place it next to start.bat
  pause
  exit /b 1
)

echo Starting SUT Shabu local server...
echo Open: http://localhost:8082/sut-shabu/
echo.

set "LOG=%APP_HOME%server.log"
echo [INFO] Writing logs to: %LOG%
echo.> "%LOG%"

REM Prefer a bundled JRE if present: portable-server\jre\bin\java.exe
set "JAVA_EXE=%APP_HOME%jre\bin\java.exe"
if exist "%JAVA_EXE%" (
  echo [INFO] Using bundled Java: %JAVA_EXE%
) else (
  set "JAVA_EXE=java"
  echo [INFO] Using system Java from PATH
)

"%JAVA_EXE%" -version >nul 2>&1
if errorlevel 1 (
  echo [ERROR] Java not found. Install Java ^(JDK/JRE^) or bundle a JRE in .\jre\
  echo [ERROR] Java not found.> "%LOG%"
  pause
  exit /b 1
)

REM --path ensures stable context path regardless of WAR name
echo [INFO] Starting Java...
echo [CMD] "%JAVA_EXE%" -jar "%APP_HOME%jetty-runner.jar" --port 8082 --path /sut-shabu "%APP_HOME%sut-shabu.war" >> "%LOG%"
"%JAVA_EXE%" -jar "%APP_HOME%jetty-runner.jar" --port 8082 --path /sut-shabu "%APP_HOME%sut-shabu.war" >> "%LOG%" 2>&1

if errorlevel 1 (
  echo.
  echo [ERROR] Server stopped with an error.
  echo Open the log file: %LOG%
)

echo.
echo Server stopped.
pause
