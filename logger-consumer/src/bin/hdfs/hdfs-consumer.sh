#!/bin/sh

base_dir=$(dirname $0)/..

# loading dependency jar in lib directory
for file in $base_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

echo $CLASSPATH


# Which java to use
if [ -z "$JAVA_HOME" ]; then
  JAVA="java"
else
  JAVA="$JAVA_HOME/bin/java"
fi
# real time msg ->storm:topic =icntv.real.time
# no real time msg ->hdfs :icntv-hdfs-group or search : icntv-search-group: topic = icntv.no.real.time

java -classpath $CLASSPATH  tv.icntv.consumer.IcntvConsumerGroup  icntv.no.real.time    4   icntv-hdfs-group