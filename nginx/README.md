# NGINX

Cambiar en el default el puerto por el que vamos a escuchar y la ruta que queremos servir

```
        listen 22280 default_server;
        listen [::]:22280 default_server;
        ...
        root /opt/vagrant;
        ..
```

    $ sudo systemctl status nginx