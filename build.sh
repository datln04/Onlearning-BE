#!/bin/sh

# Absolute path to this script, e.g. /home/user/bin/build.sh
SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPT_PATH=$(dirname "$SCRIPT")

cd "$SCRIPT_PATH";
mvn clean package -Dmaven.test.skip;

cd "$SCRIPT_PATH"/target;
java -Djarmode=layertools -jar onlearn-*.jar extract