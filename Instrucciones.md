 - Crear repostorio en GIT INETUM (BB)

 - Crear repositorio en GIT LOCAL (Gitlab) --> Vacio, sin README

 - Crear el proyecto en Jenkins
      Para proyectos FWPA:
         Copiar de otro y cambiar la URL del repositorio
   
 - En el GIT LOCAL (Gitlab): Crear Integración "jenkins"
   
   Para proyectos FWPA:
      Enable integration: Active
      Trigger: Tag push
   
   Jenkins server URL:
      http://jenkins:8080
   
   SSL Verification: Disable

   Project name: <nombre-proyecto>

   Username: gitlab

   Password: Pr0y3ct0|n3tum

 - Clonar del SVN o del GIT el codigo del proyecto y subirlo a GIT LOCAL (Gitlab)

      cd <nombre-proyecto>
      git init --initial-branch=main
      git remote add origin ssh://git@localhost:11122/awsn/<nombre-proyecto>.git
         Cambiar versión del pom padre para cambiar el plugin de gitflow por el nuevo (<version>2.0.5.1</version>)
      git add .
      git commit -m "Commit incial"
      git push --set-upstream origin main

      git tag -a <version> -m '<version>' && git push origin : <version>

      git checkout -b develop
         Dejar pom preparados. Poner SNAPSHOT
      git add .
      git commit -m "Commit incial"
      git push --set-upstream origin develop
    
   - Prepara el proyecto para INETUM
      Cambiar versión del pom padre para cambiar el plugin de gitflow por el nuevo (<version>2.0.5.1</version>)
      Cambiar el jenkinsfile
      

    



## Crear usuario JENKINS en máquina

 - Crear un par clave privada/pública en la máquina

   ssh-keygen -t ed25519 -C "marcos.pajon@inetum.com"


 - Gitlab: 
   - Crear usuario "jenkins" de tipo "desarrollador" y añadirle la clave pública generada y token de acceso.
   - Añadir el usurio jenkins al grupo AWSN

 - Jenkins: 
   - Configure the host key verification strategy from "Manage Jenkins" >> "Security" >> "Git Host Key Verification Configuration". --> NO Verification  
   - Crear credencial de tipo "SSH username and private key" con el id Jenkins_ssh_agent y la clave privada generada.
   - Crear credencial de tipo "GitLab API token"
   - Crear credencial de tipo "GitLab Personal Access Token"

## Crear Proyecto en GITLAB
 - Crear el proyecto

## Plugins Jenkins

Config File Provider Plugin
Pipeline Maven Integration
Pipeline Maven Plugin API
pipeline-utility-steps