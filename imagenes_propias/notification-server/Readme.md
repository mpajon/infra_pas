


docker build -t registry.asnw.inetum.com/notification-server:0.0.1 .

docker run -p 5342:5342 registry.asnw.inetum.com/notification-server:0.0.1
docker run -p 5342:5342 -e HTTP_PROXY=http://172.16.255.100:3128 -e HTTPS_PROXY=http://172.16.255.100:3128 registry.asnw.inetum.com/notification-server:0.0.1

docker login -u admin -p temporal1234 http://registry.asnw.inetum.com/
docker push registry.asnw.inetum.com/notification-server:0.0.1
