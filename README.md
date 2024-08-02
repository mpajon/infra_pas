# infra_pas

Crear los directorios en _$DOCKER_PATH_:

    $ gitlab/gitlab-data

    $ gitlab/gitlab-postgresql

    $ gitlab/redis-data

    $ nexus/nexus-data

    $ portainer/portainer-data

    $ sonarqube/sonarqube-postgresql-data 
    
    $ sonarqube/sonarqube_data 
    
    $ sonarqube/sonarqube_extensions 
    
    $ sonarqube/sonarqube_logs 
    
    $ sonarqube/sonarqube-postgresql

## Proxy a nivel de demonio

    Añadir en 
    
    $ /etc/systemd/system/docker.service.d/http-proxy.conf

    Recargar

    $ sudo systemctl daemon-reload

    Reiniciar

    $ sudo systemctl restart docker

## Arrancar en local

    $ docker-compose --env-file ./local.env up -d

# Nexus

Cambiar los permisos al directorio _nexus_

    $ sudo chown -R 200 ${DOCKER_PATH}/nexus

El proxy se establece en _Administration_ --> _System_ --> "Configuring HTTP Request Settings"

# Jenkins

Cambiar los permisos al direcorio _jenkins_
    
    $ sudo chown -R 1000:1000 ${DOCKER_PATH}/jenkins/

El proxy se establece en el primer arranque

Pipeline Utility Steps para poder usar readMavenPom

Scripts not permitted to use method org.apache.maven.model.Model getArtifactId --> Navigate to jenkins > Manage jenkins > In-process Script Approv

Permisos 755 a las máquinas de java

    $ sudo chmod +755 -R openjdk11/

Plugins

Config File Provider Plugin --> Para poder usar ficheros de configuración como el settings.xml de maven
Pipeline Maven Integration --> Para poder usar el withMaven en el pipeline
SSH Agent  --> para poder usar conexiones ssh
http request --> para poder hacer smoke test



Instalar en la imagen el bzip2 para poder usar phantomj en la compilación de los front

    $ docker exec -it -u root jenkins /bin/bash

    $ apt update

    $ apt-get install bzip2

Instalar rsync en la imagen para poder usar el script git2pasgit.sh

    $ docker exec -it -u root jenkins /bin/bash

    $ apt update

    $ apt-get install rsync

Para evitar problemas del tipo "fatal: not in a git directory" cuando se clona un proyecto desde un pipeline, ejecutar:

    $ git config --global --add safe.directory '*'

desde el directorio _/var/jenkins_home_

Hay una variable COMP_PROXY definida a nivel de Jenkins Panel de Control --> Administrar Jenkins --> System que se puede usar desde los pipelines:

    $ sh 'echo $COMP_PROXY

# Sonar

Ejecutar en el host:

    $ sudo sysctl -w vm.max_map_count=262144

# Gitlab

Para poder usar los webhooks entre Jenkins y Gitlab

Admin → Settings → Network → Outbound Requests → Allow requests to the local network from hooks and services
