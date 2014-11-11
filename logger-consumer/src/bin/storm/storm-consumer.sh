#!/bin/sh
base_dir=$(dirname $0)/..

# loading dependency jar in lib directory
for file in $base_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

echo $CLASSPATH

if [ -z "$CONSUMER_LOG4J_OPTS" ]; then
  CONSUMER_LOG4J_OPTS="-Dconsumer-logback=$base_dir/resources/logback.xml "
fi

if [ -z "$CONSUMER_KAFKA_OPTS" ]; then
  CONSUMER_KAFKA_OPTS="-Dconsumer-kafka=$base_dir/resources/consumer.properties "
fi

if [ -z "$JDBC_CONFIG_OPTS" ]; then
  JDBC_CONFIG_OPTS="-Djdbc-config=$base_dir/resources/jdbc.properties "
fi

# Which java to use
if [ -z "$JAVA_HOME" ]; then
  JAVA="java"
else
  JAVA="$JAVA_HOME/bin/java"
fi
# real time msg ->storm:topic =icntv.real.time
# no real time msg ->hdfs :icntv-hdfs-group or search : icntv-search-group: topic = icntv.no.real.time

java $CONSUMER_LOG4J_OPTS $JDBC_CONFIG_OPTS  $CONSUMER_KAFKA_OPTS -classpath $CLASSPATH  tv.icntv.consumer.IcntvConsumerGroup  icntv.real.time    4   icntv-storm-group  STORM_CONSUMER