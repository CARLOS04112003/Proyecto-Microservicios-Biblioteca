@echo off
setlocal EnableExtensions
set "BASE_DIR=%~dp0"
set "MAVEN_VERSION=3.9.11"
if defined MAVEN_USER_HOME (
  set "MAVEN_CACHE=%MAVEN_USER_HOME%"
) else (
  set "MAVEN_CACHE=%USERPROFILE%\.m2"
)
set "MAVEN_HOME=%MAVEN_CACHE%\wrapper\dists\apache-maven-%MAVEN_VERSION%"
set "MAVEN_BIN=%MAVEN_HOME%\apache-maven-%MAVEN_VERSION%\bin\mvn.cmd"
set "MAVEN_ZIP=%MAVEN_HOME%\apache-maven-%MAVEN_VERSION%-bin.zip"
set "MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip"
if not exist "%MAVEN_BIN%" (
  if not exist "%MAVEN_HOME%" mkdir "%MAVEN_HOME%"
  if not exist "%MAVEN_ZIP%" (
    echo Descargando Apache Maven %MAVEN_VERSION%...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%'"
    if errorlevel 1 exit /b 1
  )
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_HOME%' -Force"
  if errorlevel 1 exit /b 1
)
call "%MAVEN_BIN%" -f "%BASE_DIR%pom.xml" %*
exit /b %ERRORLEVEL%
