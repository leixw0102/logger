#!/bin/sh
base_dir=$(dirname $0)/..

# loading dependency jar in lib directory
for file in $base_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

echo $CLASSPATH

if [ -z "$CONSUMER_LOG4J_OPTS" ]; then
  CONSUMER_LOG4J_OPTS="-Dconsumer-logback=$base_dir\resources\logback.xml "
fi

if [ -z "$CONSUMER_KAFKA_OPTS" ]; then
  CONSUMER_KAFKA_OPTS="-Dconsumer-kafka=$base_dir\resources\consumer.properties "
fi

if [ -z "$CONSUMER_HDFS_STRATEGY_OPTS" ]; then
  CONSUMER_HDFS_STRATEGY_OPTS="-Dconsumer-hdfs-icntv=$base_dir\resources\icntvStb.properties "
fi

# Which java to use
#if [ -z "$JAVA_HOME" ]; then
#  JAVA="java"
#else
#  JAVA="$JAVA_HOME/bin/java"
#fi
# real time msg ->storm:topic =icntv.real.time
# no real time msg ->hdfs :icntv-hdfs-group or search : icntv-search-group: topic = icntv.no.real.time

java $CONSUMER_LOG4J_OPTS $CONSUMER_KAFKA_OPTS $CONSUMER_HDFS_STRATEGY_OPTS -classpath $CLASSPATH  tv.icntv.consumer.IcntvConsumerGroup  icntv.no.real.time    4   icntv-hdfs-group HDFS_CONSUMER