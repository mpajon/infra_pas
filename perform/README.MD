# Performa

Performa is a multi-server monitoring system with a web based front-end UI. It can monitor CPU, memory, disk, network, and of course your own custom metrics. Alerts can be configured to trigger on any expression, and send e-mails or fire web hooks.
[https://github.com/jhuckaby/performa](https://github.com/jhuckaby/performa)
[https://github.com/jhuckaby/performa-satellite](https://github.com/jhuckaby/performa-satellites)

## Instalación

NodeJS y NPM

    $ sudo apt install nodejs
    $ sudo apt install npm

Instalación de Performa

    $ sudo su
    $ curl -s https://raw.githubusercontent.com/jhuckaby/performa/master/bin/install.js | node

Instalación de performa-satellite

    $ mkdir /opt/performa
    $ curl -L https://github.com/jhuckaby/performa-satellite/releases/latest/download/performa-satellite-linux > /opt/performa/satellite.bin
    $ chmod 755 /opt/performa/satellite.bin
    $ /opt/performa/satellite.bin --install    

## Configuración

En el fichero _/opt/performa/conf/config.json_ cambiar el parámetro base_url por _https://performa.asnw.inetum.com_

Ejecutar

    $ /opt/performa/bin/control.sh setup
    $ /opt/performa/bin/control.sh start

## Control del demonio

    $ /opt/performa/bin/control.sh start
    $ /opt/performa/bin/control.sh status
    $ /opt/performa/bin/control.sh stop