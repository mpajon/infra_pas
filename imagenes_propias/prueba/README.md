docker build -t inetum/prueba:1.0-SNAPSHOT .

docker run -v /home/marcos/docker/data/maven:/root/.m2/ --name prueba inetum/prueba:1.0-SNAPSHOT