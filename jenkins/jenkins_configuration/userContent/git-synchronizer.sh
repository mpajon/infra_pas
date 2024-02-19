#!/bin/bash

DESTINO=$1
PROJECT_NAME=$2
BACKUP_MAIN_AND_TAGS_ONLY=${3:-false}

# Load environment variables from .env file
[ ! -f /var/jenkins_home/userContent/.${DESTINO}.env ] || export $(grep -v '^#' /var/jenkins_home/userContent/.${DESTINO}.env | xargs)

SOURCE_REPO_URL="${GIT_PROTOCOL_SOURCE}${GIT_USER_SOURCE}:${GIT_TOKEN_SOURCE}@${GIT_URL_SOURCE/@PROJECT_NAME@/${PROJECT_NAME}}"
BACKUP_REPO_URL="${GIT_PROTOCOL_DESTINATION}${GIT_USER_DESTINATION}:${GIT_TOKEN_DESTINATION}@${GIT_URL_DESTINATION/@PROJECT_NAME@/${PROJECT_NAME}}"
set --  ${SOURCE_REPO_URL} ${BACKUP_REPO_URL}

echo "SOURCE_REPO_URL ${SOURCE_REPO_URL}"
echo "BACKUP_REPO_URL ${BACKUP_REPO_URL}"

SOURCE_REPO_URL=$1
BACKUP_REPO_URL=$2

# Borramos
rm -Rf source_repo || exit 1
rm -Rf backup_repo || exit 1

# Clonar el repositorio fuente si aún no existe
if [ ! -d "source_repo" ]; then
    echo "Cloning source repository..."
    git clone --mirror "$SOURCE_REPO_URL" source_repo || exit 1
fi

# Clonar el repositorio de destino si aún no existe
if [ ! -d "backup_repo" ]; then
    echo "Cloning backup repository..."
    git clone "$BACKUP_REPO_URL" backup_repo || exit 1
fi

# Entrar al repositorio de destino
cd backup_repo || exit 1

# Salir del repositorio de destino y entrar al repositorio fuente
cd ../source_repo || exit 1

# Actualizar el repositorio fuente
echo "Fetching changes from source repository..."
git fetch -p origin || exit 1

# Sincronizar solo la rama principal y las etiquetas si se especifica la opción
if [ "$BACKUP_MAIN_AND_TAGS_ONLY" = "main_and_tags" ]; then
    echo "Syncing main branch and tags only..."
    git push "$BACKUP_REPO_URL" "refs/heads/main:refs/heads/main" || exit 1

    # Forzar actualización de las etiquetas
    echo "Forcing update of tags..."
    git push --force --tags "$BACKUP_REPO_URL" || exit 1

else
    echo "Syncing entire repository..."
    git push "$BACKUP_REPO_URL" --mirror || exit 1
fi

# Borramos
rm -Rf source_repo || exit 1
rm -Rf backup_repo || exit 1

echo "Backup completed successfully"