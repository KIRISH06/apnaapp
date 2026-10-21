#!/usr/bin/env sh

# System-independent Gradle wrapper script for Unix
DIR=""
case "`uname`" in
    CYGWIN* | MINGW* | MSYS*)
        DIR=`cygpath -w "$PWD"`
        ;;
    *)
        DIR="$PWD"
        ;;
esac

CLASSPATH=$DIR/gradle/wrapper/gradle-wrapper.jar

if [ -f "$CLASSPATH" ]; then
    exec java -jar "$CLASSPATH" "$@"
else
    echo "Downloading Gradle..."
    exec gradle "$@"
fi
