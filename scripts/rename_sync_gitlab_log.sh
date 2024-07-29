#!/bin/bash

# Directorio donde se encuentra el archivo log
LOG_DIR="/opt/scripts/logs"

# Nombre del archivo log actual
LOG_FILE="sync_gitlab.log"

# Obtener la fecha actual en el formato YYYY_MM_DD
DATE=$(date +"%Y_%m_%d")

# Renombrar el archivo log
mv "$LOG_DIR/$LOG_FILE" "$LOG_DIR/sync_gitlab_$DATE.log"
