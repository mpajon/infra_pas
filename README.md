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

## Arrancar en local

    $ docker-compose --env-file ./local.env up -d

# Sonar

Cambiar los permisos al directorio _nexus_

    $ sudo chown -R 200 ${DOCKER_PATH}/nexus

El proxy se establece en _Administration_ --> _System_ --> "Configuring HTTP Request Settings"

# Jenkins

Cambiar los permisos al direcorio _jenkins_
    
    $ sudo chown -R 1000:1000 ${DOCKER_PATH}/jenkins/

El proxy se establece en el primer arranque

# Sonar

Ejecutar en el host:

    $ sudo sysctl -w vm.max_map_count=262144
