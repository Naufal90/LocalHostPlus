package naufal90.localhostplus.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PlayerUUIDManager {
    public static UUID getOfflineUUID(String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        UUID fixedUUID = getOfflineUUID(player.getGameProfile().getName());
        if (player.getGameProfile().getId().version() == 0) {
            ObfuscationReflectionHelper.setPrivateValue(Player.class, player, fixedUUID, "uuid");
        }
    }
}
