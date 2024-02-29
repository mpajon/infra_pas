# Servidor 172.17.134.1 (mandalor.asnw.inetum.com)

Información sobre el servidor

## Para ver espacio de volúmenes físicos, virtuales...

    $ vgs

```
  VG        #PV #LV #SN Attr   VSize    VFree
  ubuntu-vg   1   7   0 wz--n- <298,00g <145,00g
```    

    $ pvs

```
  PV         VG        Fmt  Attr PSize    PFree
  /dev/sda3  ubuntu-vg lvm2 a--  <298,00g <145,00g
```    

    $ lvs
```
      LV     VG        Attr       LSize   Pool Origin Data%  Meta%  Move Log Cpy%Sync Convert
    homelv ubuntu-vg -wi-ao----   2,00g
    optlv  ubuntu-vg -wi-ao---- 105,00g
    rootlv ubuntu-vg -wi-ao----  30,00g
    swaplv ubuntu-vg -wi-ao----   4,00g
    tmplv  ubuntu-vg -wi-ao----   2,00g
    usrlv  ubuntu-vg -wi-ao----   5,00g
    varlv  ubuntu-vg -wi-ao----   5,00g
```

## Para ver el espacio de los directorios que mas ocupan

    $ sudo ncdu
    
    $ ncdu

## Para ampliar el espacio de los directorios
    
    $ FS="/dev/mapper/ubuntu--vg-varlv" && lvextend -L+45G $FS -r

## En local para ampliar el espacio

 - Incrementar el espacio en el vdi desde el VirtualBox
 - Desde el servidor  [https://forum.cloudron.io/topic/6086/ubuntu-20-04-how-to-extend-partition-for-noobs](https://forum.cloudron.io/topic/6086/ubuntu-20-04-how-to-extend-partition-for-noobs):
    - fdisk /dev/sda
        - p (ver las particiones)
        - d (borar la que queremos extender)
        - n (crear una nueva)
        - Usar todo el espacio
        - [n] No borrar la firma
        - w escribir
    - reinicinar
 - Desde el servidor [https://4sysops.com/archives/extending-lvm-space-in-ubuntu/](https://4sysops.com/archives/extending-lvm-space-in-ubuntu/)
        - pvresize /dev/sda3
        - lvextend -l +100%FREE -r /dev/ubuntu-vg/ubuntu-lv