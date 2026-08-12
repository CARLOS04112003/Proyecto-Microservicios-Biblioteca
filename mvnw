#!/bin/sh
set -eu
BASE_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
MAVEN_VERSION=3.9.11
MAVEN_HOME="${MAVEN_USER_HOME:-$HOME/.m2}/wrapper/dists/apache-maven-$MAVEN_VERSION"
MAVEN_BIN="$MAVEN_HOME/apache-maven-$MAVEN_VERSION/bin/mvn"
ZIP="$MAVEN_HOME/apache-maven-$MAVEN_VERSION-bin.zip"
URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip"
if [ ! -x "$MAVEN_BIN" ]; then
  mkdir -p "$MAVEN_HOME"
  if [ ! -f "$ZIP" ]; then
    echo "Descargando Apache Maven $MAVEN_VERSION..."
    if command -v curl >/dev/null 2>&1; then
      curl -fL "$URL" -o "$ZIP"
    elif command -v wget >/dev/null 2>&1; then
      wget "$URL" -O "$ZIP"
    else
      echo "Se requiere curl o wget para descargar Maven." >&2
      exit 1
    fi
  fi
  if command -v unzip >/dev/null 2>&1; then
    unzip -q -o "$ZIP" -d "$MAVEN_HOME"
  else
    echo "Se requiere unzip para extraer Maven." >&2
    exit 1
  fi
fi
exec "$MAVEN_BIN" -f "$BASE_DIR/pom.xml" "$@"
