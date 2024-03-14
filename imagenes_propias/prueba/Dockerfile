FROM inetum/openjdk11:1.0-SNAPSHOT
COPY settings.xml /root/.m2/
COPY settings.xml /usr/lib/mvn/conf/
ADD . /tmp/borrar
RUN cd /tmp/borrar && \
    mvn -X clean package