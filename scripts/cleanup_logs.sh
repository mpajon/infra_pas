#!/bin/bash

# Directorio donde se encuentran los archivos de log
LOG_DIR="/opt/scripts/logs"

# Expresión regular para los archivos de log
LOG_PATTERN="sync_gitlab_*.log"

# Número de archivos que deseas mantener
KEEP=7

# Encuentra y ordena los archivos por fecha de modificación en orden inverso
FILES=$(ls -t ${LOG_DIR}/${LOG_PATTERN})

# Contador de archivos
COUNT=0

# Recorre los archivos
for FILE in $FILES; do
    COUNT=$((COUNT+1))
    # Si el contador es mayor que el número de archivos a mantener, elimina el archivo
    if [ $COUNT -gt $KEEP ]; then
        rm -f $FILE
    fi
done

