<p align="center">
  <img src="https://raw.githubusercontent.com/Naufal90/TeleChatSRV/c6022d8dfcf5066614631cc1604aadc38d192018/.github/assets/logo.PNG" alt="TeleChatSRV Logo" width="200"/>
</p>

<h1 align="center">LocalHostPlus (Fabric Mod)</h1>

**LocalHostPlus** adalah mod Minecraft Fabric yang memungkinkan pemain membuka dunia lokal (*Open to LAN*) melalui jaringan **hotspot**, memungkinkan pemain lain terhubung tanpa perlu internet, mirip dengan mekanisme multiplayer Minecraft Bedrock (mobile).

## Fitur

- Buka dunia lokal dan siarkan ke jaringan hotspot.
- IP dan port ditampilkan secara otomatis saat server aktif.
- Tombol *Start/Stop Server* yang dinamis.
- Kompatibel dengan *Integrated Server*.
- Konfigurasi online/offline mode (dalam pengembangan).
- Dukungan `serverlan.properties` (akan datang).

## Cara Pakai

1. Install mod ini di klien host.
2. Buka dunia singleplayer.
3. Nyalakan hotspot perangkat.
4. Buka menu [Game Settings] > [Hotspot Settings].
5. Tekan **Start Server**.
6. Informasi IP dan port akan muncul di chat.
7. Pemain lain dapat bergabung melalui menu *Direct Connect* dengan IP dan port tersebut.

## Contoh Kode

**Mengaktifkan server hotspot:**
```java
server.openToLan(null, false, 25565);
Broadcaster.startBroadcast(25565);
