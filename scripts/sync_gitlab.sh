#!/bin/bash

####################################################################################################################################################################

# Configuración de las URLs de los servidores GitLab y los tokens de acceso personal
SOURCE_GITLAB_URL="https://localhost/"
SOURCE_GITLAB_TOKEN="glpat-cD4Tbvx5zsHVGphWFu-j"
DESTINATION_GITLAB_URL="https://otd.ibl-inetum.services/gitlab/"
DESTINATION_GITLAB_TOKEN="glpat-jw4XUGWX9afkRviiXTmU"

# Ruta del grupo de destino en GitLab
DESTINATION_GROUP_PATH="ol-tech-advanced-applications-northwest/principado-de-asturias"

# Parámetro para el número de proyectos por página
PER_PAGE=${1:-100}  # Valor por defecto es 100 si no se pasa un parámetro

# Definir usuario y contraseña INETUM
USERNAME="marcos.pajon@inetum.com"
PASSWORD="glpat-jw4XUGWX9afkRviiXTmU"

# Definir usuario y contraseña MANDALOR
USERNAME_MANDALOR="marcos.pajon"
PASSWORD_MANDALOR="glpat-cD4Tbvx5zsHVGphWFu-j"
####################################################################################################################################################################

# Función para URL encoding
urlencode() {
  # Reemplazar espacios por %20, @ por %40, y otros caracteres especiales según sea necesario
  echo "$1" | sed 's/%/%25/g; s/@/%40/g; s/ /%20/g; s/\\//g; s/\&/%26/g; s/ /%20/g; s/\+/%2B/g; s/\$/%%24/g; s/,/%2C/g; s/:/%3A/g; s/;/%3B/g; s/=/%3D/g; s/?/%3F/g; s/@/%40/g; s/ /%20/g; s/\!/%21/g; s/#/%23/g; s/\[/%5B/g; s/\]/%5D/g'
}

# Codificar usuario y contraseña para URL
encoded_username=$(urlencode "$USERNAME")
encoded_password=$(urlencode "$PASSWORD")
encoded_username_mandalor=$(urlencode "$USERNAME_MANDALOR")
encoded_password_mandalor=$(urlencode "$PASSWORD_MANDALOR")

# Función para obtener proyectos desde una instancia de GitLab
get_projects() {
  local gitlab_url=$1
  local gitlab_token=$2
  local page=$3
  local per_page=$4
  curl -s --insecure --noproxy '*' --header "PRIVATE-TOKEN: $gitlab_token" "$gitlab_url/api/v4/projects?page=$page&per_page=$per_page"
}

# Función para clonar un proyecto
clone_project() {
  local repo_url=$1
  local repo_url_localhost=$(echo "$repo_url" | sed 's/gitlab\.asnw\.inetum\.com/localhost/')
  local repo_url_with_credentials=$(echo "$repo_url_localhost" | sed "s|https://|https://${encoded_username_mandalor}:${encoded_password_mandalor}@|")
  local project_name=$2

  git -c http.proxy= -c http.sslVerify=false clone -q --mirror "$repo_url_with_credentials" "$project_name.git"
}

# Función para crear un proyecto en la instancia de destino bajo un grupo específico
create_project() {
  local gitlab_url=$1
  local gitlab_token=$2
  local project_name=$3
  local group_id=$4

  curl -s --insecure --request POST --header "PRIVATE-TOKEN: $gitlab_token" --header "Content-Type: application/json" \
    --data "{ \"name\": \"$project_name\", \"namespace_id\": $group_id }" \
    "$gitlab_url/api/v4/projects" > /dev/null 2>&1
}

# Función para empujar un repositorio a la instancia de destino
push_project() {
  local project_name=$1
  local destination_repo_url=$2
  url_with_credentials="https://$encoded_username:$encoded_password@$(echo "$destination_repo_url" | sed 's|https://||')"
  git --git-dir="$project_name.git" push --mirror "$url_with_credentials"
}

####################################################################################################################################################################

echo ""
echo "# Obtener el ID del grupo de destino"
# Obtener el ID del grupo de destino
destination_group=$(curl -s --insecure --header "PRIVATE-TOKEN: $DESTINATION_GITLAB_TOKEN" "$DESTINATION_GITLAB_URL/api/v4/groups/$(echo $DESTINATION_GROUP_PATH | sed 's/\//%2F/g')")
# echo "destination_group $destination_group"

destination_group_id=$(echo "$destination_group" | jq -r '.id')
echo "destination_group_id $destination_group_id"

if [ -z "$destination_group_id" ]; then
  echo "No se pudo obtener el ID del grupo de destino. Verifica la ruta del grupo."
  exit 1
fi

echo "# Obtener la lista de proyectos desde la instancia fuente"
# Inicializar el contador
contador=0
page=1

# Obtener la lista de proyectos desde la instancia fuente
while : ; do
  response=$(get_projects "$SOURCE_GITLAB_URL" "$SOURCE_GITLAB_TOKEN" "$page" "$PER_PAGE")
  if [[ -z "$response" || "$response" == "[]" ]]; then
    break
  fi

  # Contar el total de proyectos obtenidos
  total_page=$(echo "$response" | jq '. | length')

  echo "$response" | jq -c '.[]' | while read project; do
    #echo "$project"
    ((contador++))
    project_id=$(echo "$project" | jq -r '.id')
    project_name=$(echo "$project" | jq -r '.path')
    repo_url=$(echo "$project" | jq -r '.http_url_to_repo')
    echo ""
    echo "# ---------------------------------------"
    echo "# Sincronizando proyecto $contador/$total_page : $project_name"
    echo "# ---------------------------------------"

    # Clonar el proyecto desde la instancia fuente
    echo "# Clonar el proyecto desde la instancia fuente"
    echo "# clone_project $repo_url $project_name"
    clone_project "$repo_url" "$project_name"

    # Comprobar si el proyecto existe en la instancia de destino
    echo "# Comprobar si el proyecto existe en la instancia de destino"
    destination_project=$(curl -s --insecure --header "PRIVATE-TOKEN: $DESTINATION_GITLAB_TOKEN" "$DESTINATION_GITLAB_URL/api/v4/projects/$(echo $DESTINATION_GROUP_PATH/$project_name | sed 's/\//%2F/g')")
    echo "destination_project $(echo $destination_project | jq -r '.message')"
    echo "# destination_project.message: $(echo "$destination_project" | jq -r '.message')"

    if [[ $(echo "$destination_project" | jq -r '.message') == "404 Project Not Found" || $(echo "$destination_project" | jq -r '.error') == "404 Not Found" ]]; then
      echo "# create_project $DESTINATION_GITLAB_URL $DESTINATION_GITLAB_TOKEN $project_name $destination_group_id"
      create_project "$DESTINATION_GITLAB_URL" "$DESTINATION_GITLAB_TOKEN" "$project_name" "$destination_group_id"
    fi

    # Obtener la URL del repositorio de destino
    echo "# Obtener la URL del repositorio de destino"
    destination_repo_url=$(curl -s --insecure --header "PRIVATE-TOKEN: $DESTINATION_GITLAB_TOKEN" "$DESTINATION_GITLAB_URL/api/v4/projects/$(echo $DESTINATION_GROUP_PATH/$project_name | sed 's/\//%2F/g')" | jq -r '.http_url_to_repo')

    # Empujar el proyecto al servidor de destino  
    echo "# Empujar el proyecto al servidor de destino"
    echo "# project_name $project_name"
    echo "# destination_repo_url $destination_repo_url"
    push_project "$project_name" "$destination_repo_url"

    # Limpiar el repositorio clonado localmente
    rm -rf "$project_name.git"
  done

  ((page++))
done

echo ""
echo "Sincronización completada."
