#!/bin/sh
## -workers 10 -p 14630
##workers:  the thread size of start scribe server ,default 10
##p: the port of scribe server listen,default 14630
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

java -classpath $CLASSPATH  tv.icntv.logger.Main