#!/bin/sh
base_dir=$(dirname $0)/..

# loading dependency jar in lib directory
for file in $base_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

echo $CLASSPATH

if [ -z "$QQ_CONFIG_OPTS" ]; then
  QQ_CONFIG_OPTS="-Dqq-config=$base_dir/config/ "
fi

#if [ -z "$PUBLISH_KAFKA_OPTS" ]; then
#  PUBLISH_KAFKA_OPTS="-Dpublish-kafka=$base_dir/config/kafka-producer.properties "
#fi
# Which java to use
if [ -z "$JAVA_HOME" ]; then
  JAVA="java"
else
  JAVA="$JAVA_HOME/bin/java"
fi

java $QQ_CONFIG_OPTS  -classpath $CLASSPATH  tv.icntv.log.IcntvScribeServer 14630 20 >> ./qq-log.log &