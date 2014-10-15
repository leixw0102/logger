@echo off

setlocal enabledelayedexpansion
set BASE_DIR=%CD%\..
set CLASSPATH=
echo %BASE_DIR%


for %%i in (%BASE_DIR%\lib\*.jar) DO SET CLASSPATH=!CLASSPATH!;%%i


echo %CLASSPATH%

IF ["%JAVA_HOME%"] EQU [""] (
        set JAVA=java
) ELSE (
        set JAVA="%JAVA_HOME%/bin/java"
)

%JAVA% -CLASSPATH %CLASSPATH% tv.icntv.logger.Main