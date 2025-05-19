package naufal90.localhostplus.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.server.integrated.IntegratedServer;
import naufal90.localhostplus.LocalHostPlusMod;

public class HotspotServer {

    public static void openToLan() {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client.getServer() instanceof IntegratedServer) {
        IntegratedServer server = (IntegratedServer) client.getServer();
        try {
            server.openToLan(server.getSaveProperties().getGameMode(), server.getSaveProperties().isHardcore(), 25565);
            LocalHostPlusMod.LOGGER.info("[Hotspot] Server published on LAN!");

            String username = client.getSession().getUsername(); // Tambahkan ini
            Broadcaster.startBroadcast(username, server.getServerPort()); // Gunakan username
        } catch (Exception e) {
            LocalHostPlusMod.LOGGER.error("[Hotspot] Failed to open LAN: ", e);
        }
    }
    }
    
    public static void start(String username, MinecraftServer server) {
        Broadcaster.startBroadcast(username, server.getServerPort());
    }
    
}
