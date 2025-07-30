@echo off
setlocal

REM runtime フォルダをベースパスとして設定
set "BASE_DIR=%~dp0runtime"

REM JavaFX SDK の lib フォルダ
set "JFX_SDK=%BASE_DIR%\javafx-sdk"
set "JFX_LIB=%JFX_SDK%\lib"

REM バンドル済み JRE の java.exe
set "JAVA_EXE=%BASE_DIR%\bin\javaw.exe"

REM 実行する JAR
set "APP_JAR=%~dp0ecosim.jar"

REM JavaFX モジュール指定で起動
"%JAVA_EXE%" ^
  --module-path "%JFX_LIB%" ^
  --add-modules javafx.controls,javafx.fxml ^
  -jar "%APP_JAR%" %*

endlocal