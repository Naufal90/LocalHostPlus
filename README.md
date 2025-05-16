
<p align="center">
  <img src=".github/workflows/assets/logo.png" alt="LocalHostPlus Logo" width="200"/>
</p>

<h1 align="center">LocalHostPlus (Fabric Mod)</h1>
<p align="center"><i>Mengubah dunia Minecraft Java menjadi server hotspot lokal</i></p>

---

## Tentang

**LocalHostPlus** adalah mod untuk Minecraft (Fabric) yang memungkinkan pemain membuka dunia *Singleplayer* sebagai server lokal melalui jaringan **hotspot**, tanpa memerlukan koneksi internet. Mirip dengan pengalaman *LAN multiplayer* di Minecraft Bedrock Edition (mobile).

---

## Fitur Utama

- Host dunia lokal melalui hotspot (tanpa WiFi atau internet)
- IP dan port ditampilkan otomatis saat server aktif
- Tombol *Start/Stop Server* langsung di dalam game menu
- Kompatibel dengan Integrated Server Minecraft
- Rencana dukungan `serverlan.properties` untuk konfigurasi lanjutan
- (WIP) Mode online/offline dengan UUID Fixer

---

## Cara Menggunakan

1. Instal mod ini pada perangkat host (client Fabric).
2. Buka dunia Singleplayer.
3. Aktifkan hotspot perangkat Anda.
4. Buka menu game â†’ *Hotspot Settings*.
5. Klik tombol **Start Server**.
6. IP dan port akan ditampilkan di dalam chat.
7. Pemain lain dapat bergabung melalui *Direct Connect* dengan IP tersebut.

---

## Contoh Kode

```java
// Menyalakan LAN server dan mulai broadcast IP ke jaringan
server.openToLan(null, false, 25565);
Broadcaster.startBroadcast(25565);
```

---

## Kompatibilitas

- **Minecraft:** 1.20.1 (support untuk versi lain akan menyusul)
- **Loader:** Fabric
- **Tidak kompatibel:** Forge (saat ini)

---

## Kontribusi

Kontribusi terbuka! Jika kamu ingin membantu mengembangkan, memperbaiki bug, atau menambahkan fitur:
- Fork repositori ini
- Buat branch baru untuk fitur/bugfix kamu
- Lakukan pull request

---

## Lisensi

Mod ini dilisensikan di bawah lisensi **MIT**. Lihat `LICENSE` untuk detailnya.

---

<p align="center">
  Dibuat dengan semangat LAN party klasik
</p>
