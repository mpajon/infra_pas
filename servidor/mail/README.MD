<<<<<<< HEAD
# Para enviar correos eléctronicos 

## Requisitos

Es necesario tener una cuenta de google con la verificación en dos pasos activada y una constraseña de aplicación:
- [Crear cuenta de Google](https://support.google.com/mail/answer/56256?hl=es)
- [Activar la verificación en dos pasos](https://support.google.com/accounts/answer/185839)
- [Iniciar sesión con contraseñas de aplicación](https://support.google.com/accounts/answer/185833)

## Instalación

- Instalamos las dependencias necesarias:

        $ sudo apt install postfix mailutils

    Elegimos "Internet Site" y el nombre del servidor.

## Configuración

- Pasamos a super usuario

            $ sudo su

- Configuramos el fichero _/etc/postfix/main.cf_ añadiendo al final (y borrando la propiedad _relayhost_ existente):

```
proxy_interfaces = 172.16.255.100:3128
# GMAIL
    relayhost = [smtp.gmail.com]:587
    smtp_use_tls = yes
    smtp_sasl_auth_enable = yes
    smtp_sasl_security_options = noanonymous
    smtp_sasl_password_maps = hash:/etc/postfix/sasl_passwd
    smtp_tls_CAfile = /etc/ssl/certs/ca-certificates.crt
```


- Creamos el fichero _/etc/postfix/sasl_passwd_

```
[smtp.gmail.com]:587 <mail>:<app password>
```
- Generamos el hash

        $ postmap /etc/postfix/sasl_passwd

- Probamos la configuración 
        
        $ postfix check

- Reiniciamos el Servicio

        $ systemctl restart postfix

- Probamos el envio de correo

        $ echo "This is a test email." | mail -s "Test Email" marcos.pajon@inetum.com

        $ echo "This is a test email." | mail -s "Test Email" mpajon@gmail.com

    _(Puede estar en SPAM)_

- Para ver los logs:

        $ tail /var/log/mail.log

=======
# Para enviar correos eléctronicos

## Requisitos

Es necesario tener una cuenta de google con la verificación en dos pasos activada y una constraseña de aplicación:
- [Crear cuenta de Google](https://support.google.com/mail/answer/56256?hl=es)
- [Activar la verificación en dos pasos](https://support.google.com/accounts/answer/185839)
- [Iniciar sesión con contraseñas de aplicación](https://support.google.com/accounts/answer/185833)

## Instalación

- Instalamos las dependencias necesarias:

        $ sudo apt install postfix mailutils

    Elegimos "Internet Site" y el nombre del servidor.

## Configuración

- Pasamos a super usuario

            $ sudo su

- Configuramos el fichero _/etc/postfix/main.cf_ añadiendo al final (y borrando la propiedad _relayhost_ existente):

```
proxy_interfaces = 172.16.255.100:3128
# GMAIL
    relayhost = [smtp.gmail.com]:587
    smtp_use_tls = yes
    smtp_sasl_auth_enable = yes
    smtp_sasl_security_options = noanonymous
    smtp_sasl_password_maps = hash:/etc/postfix/sasl_passwd
    smtp_tls_CAfile = /etc/ssl/certs/ca-certificates.crt
```


- Creamos el fichero _/etc/postfix/sasl_passwd_

```
[smtp.gmail.com]:587 <mail>:<app password>
```
- Generamos el hash

        $ postmap /etc/postfix/sasl_passwd

- Probamos la configuración

        $ postfix check

- Reiniciamos el Servicio

        $ systemctl restart postfix

- Probamos el envio de correo

        $ echo "This is a test email." | mail -s "Test Email" marcos.pajon@inetum.com

        $ echo "This is a test email." | mail -s "Test Email" mpajon@gmail.com

    _(Puede estar en SPAM)_

- Para ver los logs:

        $ tail /var/log/mail.log

>>>>>>> 652712bdf4327dbdef86f47b0b5d9e58c50e7a6e
Fuente: [https://tecadmin.net/configure-postfix-use-gmail-smtp-ubuntu/](https://tecadmin.net/configure-postfix-use-gmail-smtp-ubuntu/)
