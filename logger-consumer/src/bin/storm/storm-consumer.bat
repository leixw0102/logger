@echo off

set BASE_DIR=%CD%\..
set CLASSPATH=
echo %BASE_DIR%


for %%i in (%BASE_DIR%\lib\*.jar) DO SET CLASSPATH=!CLASSPATH!;%%i


echo %CLASSPATH%


java -Dconsumer-logback=%BASE_DIR%\resources\logback.xml -Dconsumer-kafka=%BASE_DIR%\resources\consumer.properties -Djdbc-config=%BASE_DIR%\resources\jdbc.properties -classpath %CLASSPATH% tv.icntv.consumer.IcntvConsumerGroup  icntv.real.time    4   icntv-storm-group   STORM_CONSUMER