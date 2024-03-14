#!/usr/bin/env bash

sudo -u jboss mkdir -p /opt/jboss/WEBAPPS/services/gepep/logs
sudo -u jboss mkdir -p /opt/jboss/WEBAPPS/services/gepep && cd /opt/jboss/WEBAPPS/services/gepep

sudo -u root ln -s /vagrant/src/conf config

# Certificado del LDAP
sudo /opt/jboss/jdk11/bin/keytool -delete -alias ldapeducainteg -keystore /opt/jboss/jdk11/lib/security/cacerts -storepass changeit >/dev/null
sudo /opt/jboss/jdk11/bin/keytool -import -trustcacerts -keystore /opt/jboss/jdk11/lib/security/cacerts -storepass changeit -noprompt -alias ldapeducainteg -file /vagrant/src/conf/ldapeducainteg.cer

# Certificado Keycloak
sudo -u jboss /opt/jboss/jdk11/bin/keytool -delete -alias ov01lnxvue -keystore /opt/jboss/cert/truststore-cacerts.jks -storepass changeit >/dev/null
sudo -u jboss /opt/jboss/jdk11/bin/keytool -import -trustcacerts -keystore /opt/jboss/cert/truststore-cacerts.jks -storepass changeit -noprompt -alias ov01lnxvue -file /vagrant/src/conf/ov01lnxvue.princast.org.cer