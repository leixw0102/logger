@echo off

setlocal enabledelayedexpansion
set BASE_DIR=%CD%\..
set CLASSPATH=
echo %BASE_DIR%


for %%i in (%BASE_DIR%\lib\*.jar) DO SET CLASSPATH=!CLASSPATH!;%%i


echo %CLASSPATH%


java -classpath %CLASSPATH% tv.icntv.consumer.IcntvConsumerGroup  icntv.no.real.time    4   icntv-hdfs-group HDFS_CONSUMER