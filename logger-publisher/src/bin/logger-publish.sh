#!/bin/sh
base_dir=$(dirname $0)/..

# loading dependency jar in lib directory
for file in $base_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

echo $CLASSPATH

if [ -z "$PUBLISH_LOG4J_OPTS" ]; then
  PUBLISH_LOG4J_OPTS="-Dpublish-logback=$base_dir/config/logback.xml "
fi

if [ -z "$PUBLISH_KAFKA_OPTS" ]; then
  PUBLISH_KAFKA_OPTS="-Dpublish-kafka=$base_dir/config/kafka-producer.properties "
fi
# Which java to use
if [ -z "$JAVA_HOME" ]; then
  JAVA="java"
else
  JAVA="$JAVA_HOME/bin/java"
fi

java $PUBLISH_LOG4J_OPTS $PUBLISH_KAFKA_OPTS -classpath $CLASSPATH  tv.icntv.logger.Main -workers 100 >> ./publish.log &