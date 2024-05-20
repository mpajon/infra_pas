#!/bin/bash

PROJECT_NAME=$1
VERSION=$2

# Load environment variables from .env file
[ ! -f /var/jenkins_home/userContent/.pas.env ] || export $(grep -v '^#' /var/jenkins_home/userContent/.pas.env | xargs)
#[ ! -f .pas.env ] || export $(grep -v '^#' .pas.env | xargs)

SOURCE_REPO_URL="${GIT_PROTOCOL_SOURCE}${GIT_USER_SOURCE}:${GIT_TOKEN_SOURCE}@${GIT_URL_SOURCE}${PROJECT_NAME}"
DESTINATION_REPO_URL="${GIT_PROTOCOL_DESTINATION}${GIT_USER_DESTINATION}:${GIT_TOKEN_DESTINATION}@${GIT_URL_DESTINATION}${PROJECT_NAME}"

echo "SOURCE_REPO_URL ${SOURCE_REPO_URL}"
echo "DESTINATION_REPO_URL ${DESTINATION_REPO_URL}"

# Borramos
rm -Rf source_repo || exit 1
rm -Rf destination_repo || exit 1

# Clonar el repositorio destino si aún no existe
if [ ! -d "destination_repo" ]; then
    echo "Cloning destination repository..."
    git config --global core.autocrlf input
    git config --global http.sslVerify false
    git config --global http.proxy ""
    git clone "$DESTINATION_REPO_URL" destination_repo || exit 1
    cd destination_repo || exit 1
    git checkout desarrollo || exit 1
    cd .. || exit 1
    echo "----------------------------"
fi

# Clonar el repositorio de origen si aún no existe
if [ ! -d "source_repo" ]; then
    echo "Cloning source repository..."
    git config --global core.autocrlf input
    git clone "$SOURCE_REPO_URL" source_repo || exit 1
    cd source_repo || exit 1
    git -c advice.detachedHead=false checkout tags/"$VERSION" || exit 1
    echo "----------------------------"
fi

# Usando rsync sincronizamos las carpetas
if [ ! -d "source_repo" ]; then
    echo "Rsync..."
    rm -Rf .git || exit 1
    cd .. || exit 1
    rsync -av --delete --exclude='.git' source_repo/ destination_repo  || exit 1
    # Para asegurarnos que se copian todos los ficheros de un repo a otro
    diff -r --exclude='.git' source_repo destination_repo || exit 1
    echo "----------------------------"
fi

# Crear merge-request
echo "Creating merge request..."
cd destination_repo || exit 1
git config --global http.sslVerify false
git config --global http.proxy ""
git config --global user.email "marcos.pajon@inetum.com" || exit 1
git config --global user.name "Marcos Pajón" || exit 1
# Para evitar el warning de CRLF will be replaced by LF
git config --global core.safecrlf false
# Para prevenir el 100644 → 100755 commits
git config --global core.filemode false
git add .
git status
git add . && git commit -m "$VERSION" || exit 1
git push || exit 1
echo "Merge request create successfully"
cd ..  || exit 1
echo "----------------------------"

# Borramos
rm -Rf source_repo || exit 1
rm -Rf destination_repo || exit 1