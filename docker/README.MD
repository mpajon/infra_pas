sudo  systemctl status docker.service
sudo  systemctl daemon-reload
sudo  systemctl restart docker.service
sudo  systemctl status docker.service

## Para el demonio de docker
    
    $ sudo systemctl stop docker.service
    $ sudo systemctl stop docker.socket