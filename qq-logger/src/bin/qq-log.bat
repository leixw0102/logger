@echo off

setlocal enabledelayedexpansion
set BASE_DIR=%CD%\..
set CLASSPATH=
echo %BASE_DIR%


for %%i in (%BASE_DIR%\lib\*.jar) DO SET CLASSPATH=!CLASSPATH!;%%i


echo %CLASSPATH%

java   -Dpublish-logback=%BASE_DIR%\config\logback.xml -Dpublish-kafka=%BASE_DIR%\config\kafka-producer.properties -classpath %CLASSPATH% tv.icntv.logger.Main