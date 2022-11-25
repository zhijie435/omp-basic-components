#!/bin/sh

set -eo pipefail

if [ "$JAR_NAME" ]
then
    if [ "$JAVA_OPTS" ]
    then
        exec /usr/bin/java $JAVA_OPTS -jar $JAR_NAME
    else
        exec /usr/bin/java -Xdebug -Xms1g -Xmx2g -jar $JAR_NAME
    fi
else
   echo 'Please set env JAR_NAME'
   exit 1
fi
