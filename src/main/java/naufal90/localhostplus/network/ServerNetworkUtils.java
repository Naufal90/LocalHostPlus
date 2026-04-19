package naufal90.localhostplus.network;

import net.minecraft.server.MinecraftServer;

public class ServerNetworkUtils {
    public static void startBroadcast(String uuid, MinecraftServer server) {
        Broadcaster.startBroadcast(uuid, server.getServerPort());
    }
}
