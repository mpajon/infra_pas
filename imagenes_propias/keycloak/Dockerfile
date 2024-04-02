FROM quay.io/keycloak/keycloak:15.0.2

# CLAVE SPI
RUN mkdir -p /opt/jboss/keycloak/modules/org/keycloak/samlClaveV2/main/
ADD ./resources/libs/external /opt/jboss/keycloak/modules/org/keycloak/samlClaveV2/main/ 
ADD ./resources/libs/propias /opt/jboss/keycloak/modules/org/keycloak/samlClaveV2/main/
COPY ./resources/samlClaveV2/module.xml /opt/jboss/keycloak/modules/org/keycloak/samlClaveV2/main/

# CLAVE THEME
RUN mkdir -p /opt/jboss/keycloak/modules/org/keycloak/samlClave/theme/main/
ADD ./resources/libs/propias /opt/jboss/keycloak/modules/org/keycloak/samlClave/theme/main
ADD ./resources/samlClave/module.xml /opt/jboss/keycloak/modules/org/keycloak/samlClave/theme/main

# J2EE THEME
RUN mkdir -p /opt/jboss/keycloak/modules/org/keycloak/j2ee/theme/main
COPY ./resources/themes/j2ee/keycloak-j2ee-theme.jar /opt/jboss/keycloak/modules/org/keycloak/j2ee/theme/main 
COPY ./resources/themes/j2ee/module.xml /opt/jboss/keycloak/modules/org/keycloak/j2ee/theme/main

# EDUCACION THEME
RUN mkdir -p /opt/jboss/keycloak/modules/org/keycloak/educacion/theme/main
ADD ./resources/themes/educacion/keycloak-educacion-theme.jar /opt/jboss/keycloak/modules/org/keycloak/educacion/theme/main 
COPY ./resources/themes/educacion/module.xml /opt/jboss/keycloak/modules/org/keycloak/educacion/theme/main

EXPOSE 8080
EXPOSE 8443
ENTRYPOINT ["/opt/jboss/tools/docker-entrypoint.sh", "-b", "0.0.0.0"]