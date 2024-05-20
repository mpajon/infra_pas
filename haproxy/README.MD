# HAProxy

Fichero de configuración

    $ /etc/haproxy/haproxy.cfg

Probar la configuración

    $ sudo haproxy -f /etc/haproxy/haproxy.cfg -c

    $ sudo systemctl restart haproxy

    $ sudo systemctl status haproxy

Log

    /etc/rsyslog.conf

    Descomentar
    
    # provides TCP syslog reception
    module(load="imtcp")
    input(type="imtcp" port="514")

    $ sudo systemctl restart rsyslog

Probar:
    
    $ echo "prueba" | nc 127.0.0.1 514

    $ tail -f /var/log/syslog

    $ sudo journalctl -u haproxy.service -f