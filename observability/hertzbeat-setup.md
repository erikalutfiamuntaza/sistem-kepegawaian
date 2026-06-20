# Apache HertzBeat Setup

## Tujuan

Melakukan monitoring kesehatan layanan backend.

## Endpoint Monitoring

http://host.docker.internal:8080/management/health

## Konfigurasi

* Protocol : HTTP
* Method : GET
* Interval : 60 detik

## Hasil

HertzBeat berhasil:

* Mengakses endpoint health
* Menampilkan status layanan
* Mendeteksi kondisi UP dan DOWN secara real-time
