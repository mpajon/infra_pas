# Docker

## Para el demonio de docker
    
    $ sudo systemctl stop docker.service
    $ sudo systemctl stop docker.socket

## Arrancar el demonio de docker
    
    $ sudo systemctl start docker.service

## Sobreescritura de la configuración del demonio

    $ /etc/systemd/system/docker.service.d/override.conf

## Configuración del demonio de docker

    $ /etc/docker/daemon.json    

```
{
  "hosts":
   [
    "tcp://0.0.0.0:2375",
    "unix:///var/run/docker.sock"
   ],
  "proxies":
   {
    "http-proxy": "http://172.16.255.100:3128/",
    "https-proxy": "http://172.16.255.100:3128"
   }
}   
```

## Si queremos cambiar la ruta de docker

Añadir al fichero _/etc/docker/daemon.json_

```
   "data-root": "/docker/daemon"
```

## Copiar configuración de docker de _origen_ a _destino_

    $ sudo rsync -aqxP origen destino

    $ sudo rsync -aqxP /docker/daemon /var/lib/docker/