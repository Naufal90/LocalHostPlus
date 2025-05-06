package naufal90.localhostplus.utils;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PlayerUUIDManager {

    public static UUID getOfflineUUID(String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
    }

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            UUID calculatedUUID = getOfflineUUID(player.getGameProfile().getName());

            // Contoh: cetak UUID Offline jika ingin membandingkan
            System.out.println("Real UUID: " + player.getUuidAsString());
            System.out.println("Offline UUID (calculated): " + calculatedUUID.toString());

            // Jangan ubah UUID player — bisa menyebabkan error autentikasi!
            // Jika perlu UUID offline, gunakan untuk penyimpanan kustom saja
        });
    }
}
