#!/bin/sh

# Gradle wrapper script
# This script is used to run Gradle commands

GRADLE_VERSION=7.5.1
JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java-17-openjdk-amd64}

if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME is not set"
    exit 1
fi

if [ ! -d "$JAVA_HOME" ]; then
    echo "JAVA_HOME directory does not exist: $JAVA_HOME"
    exit 1
fi

exec $JAVA_HOME/bin/java -jar gradle-wrapper.jar "$@"
