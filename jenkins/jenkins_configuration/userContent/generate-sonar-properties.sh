#!/bin/bash

# Leer el package.json
PACKAGE_JSON_PATH="package.json"

# Comprobar si el archivo package.json existe
if [ ! -f "$PACKAGE_JSON_PATH" ]; then
  echo "Error: package.json no encontrado."
  exit 1
fi

# Extraer propiedades necesarias del package.json usando grep y sed
PROJECT_KEY=$(grep '"name"' "$PACKAGE_JSON_PATH" | sed -E 's/.*: *"([^"]+)".*/\1/' | head -n 1)
PROJECT_NAME=$PROJECT_KEY
PROJECT_VERSION=$(grep '"version"' "$PACKAGE_JSON_PATH" | sed -E 's/.*: *"([^"]+)".*/\1/' | head -n 1)
SOURCE_ENCODING="UTF-8"
SOURCES="src"

# Contenido del archivo sonar-project.properties
SONAR_PROPERTIES_CONTENT=$(cat <<EOF
sonar.scm.disabled=true
sonar.projectKey=${PROJECT_KEY}
sonar.projectName=${PROJECT_NAME}
sonar.projectVersion=${PROJECT_VERSION}
sonar.sourceEncoding=${SOURCE_ENCODING}
sonar.sources=${SOURCES}
sonar.exclusions=**/node_modules/**
EOF
)

# Escribir el archivo sonar-project.properties
SONAR_PROPERTIES_PATH="sonar-project.properties"
echo "$SONAR_PROPERTIES_CONTENT" > "$SONAR_PROPERTIES_PATH"

echo "Archivo sonar-project.properties generado correctamente"

