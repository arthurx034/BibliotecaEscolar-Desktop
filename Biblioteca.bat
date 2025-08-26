@echo off
REM ===================================================================
REM Script para executar o aplicativo BibliotecaEscolar JavaFX
REM ===================================================================

REM Caminho para o JAR do projeto
set JAR=target\Biblioteca-1.0-SNAPSHOT.jar

REM Caminho para os JARs do JavaFX
set JAVAFX_LIB=%~dp0javafx-lib

REM Caminho para o Java (ajuste se o seu JDK estiver em outro lugar)
set JAVA_HOME=C:\Program Files\Java\jdk-24.0.2
set JAVA_BIN=%JAVA_HOME%\bin\java.exe

REM Verifica se o JAR existe
if not exist "%~dp0%JAR%" (
    echo ERRO: Arquivo JAR nao encontrado: %JAR%
    echo Rode "mvn clean package" antes de executar este script.
    pause
    exit /b
)

REM Verifica se a pasta do JavaFX existe
if not exist "%JAVAFX_LIB%" (
    echo ERRO: Pasta com JavaFX nao encontrada: %JAVAFX_LIB%
    echo Copie os JARs do JavaFX para esta pasta.
    pause
    exit /b
)

REM Executa o JAR com JavaFX (aspas aqui, não na variável)
"C:\Program Files\Java\jdk-24_windows-x64_bin\jdk-24.0.2\bin\java.exe" --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -jar "%~dp0%JAR%"

pause
