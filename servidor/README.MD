
# Para ver espacio de volúmenes físicos, virtuales...

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

# Para ver el espacio de los directorios que mas ocupan

    $ ncdu

# Para ampliar el espacio de los directorios
    
    $ FS="/dev/mapper/ubuntu--vg-varlv" && lvextend -L+45G $FS -r