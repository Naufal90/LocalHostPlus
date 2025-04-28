package naufal90.localhostplus.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.server.integrated.IntegratedServer;
import naufal90.localhostplus.LocalHostPlusMod;

public class HotspotServer {

    public static void openToLan() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getServer() instanceof IntegratedServer server) {
            try {
                server.openToLan(server.getSaveProperties().getGameMode(), server.getSaveProperties().isHardcore(), 25565);
                LocalHostPlusMod.LOGGER.info("[Hotspot] Server published on LAN!");

                Broadcaster.startBroadcast(server.getServerPort());
            } catch (Exception e) {
                LocalHostPlusMod.LOGGER.error("[Hotspot] Failed to open LAN: ", e);
            }
        }
    }
}
