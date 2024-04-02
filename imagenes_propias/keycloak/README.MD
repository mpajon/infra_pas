## Para crear la imagen
docker build -t registry.asnw.inetum.com/keycloak-clave:15.0.2.1 .

## Para subir la imagen
docker push registry.asnw.inetum.com/keycloak-clave:15.0.2.1

## Para correr la imagen
docker run --name prueba registry.asnw.inetum.com/keycloak-clave:15.0.2.1



docker stop prueba && docker rm prueba
docker exec -it prueba /bin/bash

## Para crear la imagen
docker build -t prueba-keycloak-clave:15.0.2 .

## Para correr la imagen
docker run --name prueba prueba-keycloak-clave:15.0.2
