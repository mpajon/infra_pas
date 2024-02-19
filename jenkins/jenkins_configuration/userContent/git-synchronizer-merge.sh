#!/bin/bash

PROJECT_NAME=$1
VERSION=$2
#COMMENT=$3

# Load environment variables from .env file
 [ ! -f /var/jenkins_home/userContent/.merge.env ] || export $(grep -v '^#' /var/jenkins_home/userContent/.merge.env | xargs)
#[ ! -f .merge.env ] || export $(grep -v '^#' .merge.env | xargs)

SOURCE_REPO_URL="${GIT_PROTOCOL_SOURCE}${GIT_USER_SOURCE}:${GIT_TOKEN_SOURCE}@${GIT_URL_SOURCE/@PROJECT_NAME@/${PROJECT_NAME}}"
DESTINATION_REPO_URL="${GIT_PROTOCOL_DESTINATION}${GIT_USER_DESTINATION}:${GIT_TOKEN_DESTINATION}@${GIT_URL_DESTINATION/@PROJECT_NAME@/${PROJECT_NAME}}"

echo "SOURCE_REPO_URL ${SOURCE_REPO_URL}"
echo "DESTINATION_REPO_URL ${DESTINATION_REPO_URL}"

# Borramos
rm -Rf source_repo || exit 1
rm -Rf destination_repo || exit 1

# Clonar el repositorio destino si aún no existe
if [ ! -d "destination_repo" ]; then
    echo "Cloning destination repository..."
    git clone "$DESTINATION_REPO_URL" destination_repo || exit 1
    cd destination_repo || exit 1
    git checkout desarrollo || exit 1
    cd .. || exit 1
    echo "----------------------------"
fi

# Clonar el repositorio de origen si aún no existe
if [ ! -d "source_repo" ]; then
    echo "Cloning source repository..."
    git clone "$SOURCE_REPO_URL" source_repo || exit 1
    cd source_repo || exit 1
    git -c advice.detachedHead=false checkout tags/"$VERSION" || exit 1
    rm -Rf .git || exit 1
    cp -R * ../destination_repo  || exit 1
    cd .. || exit 1
    echo "----------------------------"
fi

# Crear merge-request
echo "Creating merge request..."
cd destination_repo || exit 1
git config --global user.email "marcos.pajon@inetum.com" || exit 1
git config --global user.name "Marcos Pajón" || exit 1
git add . && git commit -m "$VERSION" || exit 1
git push || exit 1
#git push --set-upstream origin desarrollo -o merge_request.create -o merge_request.target=integracion || exit 1
echo "Merge request create successfully"
cd ..  || exit 1
echo "----------------------------"

# Borramos
rm -Rf source_repo || exit 1
rm -Rf destination_repo || exit 1