# Observability

Folder ini berisi dokumentasi implementasi observability pada Sistem Informasi Kepegawaian menggunakan Apache SkyWalking dan Apache HertzBeat.

## Komponen

### Apache SkyWalking

Digunakan untuk melakukan distributed tracing, monitoring performa aplikasi, dan visualisasi alur request.

### Apache HertzBeat

Digunakan untuk memonitor kesehatan layanan melalui endpoint `/management/health`.

## Hasil Implementasi

* Backend berhasil terhubung ke SkyWalking.
* Trace request berhasil ditampilkan pada dashboard SkyWalking.
* Endpoint health berhasil dimonitor menggunakan HertzBeat.
* Status layanan dapat dipantau secara real-time dalam kondisi UP maupun DOWN.
