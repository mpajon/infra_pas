# Generación del certificado

Para generar el certificado que se expone en el HAProxy con los parámetros extendidos

    $ openssl genrsa -des3 -out inetumasnw.key 2048

    $ openssl req -new -key inetumasnw.key -out inetumasnw.csr

    $ cp inetumasnw.key inetumasnw.key.org

    $ openssl rsa -in inetumasnw.key.org -out inetumasnw.key

    $ openssl x509 -req -in inetumasnw.csr -signkey inetumasnw.key -out inetumasnw.crt -days 3650 -sha256 -extfile v3.ext

    $ cat inetumasnw.crt inetumasnw.key | tee asnw.inetum.pem


[https://gist.github.com/KeithYeh/bb07cadd23645a6a62509b1ec8986bbc](https://gist.github.com/KeithYeh/bb07cadd23645a6a62509b1ec8986bbc)