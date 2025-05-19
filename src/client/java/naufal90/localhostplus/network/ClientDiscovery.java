package naufal90.localhostplus.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import naufal90.localhostplus.LocalHostPlusClientMod;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Environment(EnvType.CLIENT)
public class ClientDiscovery {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static boolean running = false;

    public static void init() {
        startListener();
    }

    private static void startListener() {
        if (running) return;
        running = true;
        executor.execute(() -> {
            try (DatagramSocket socket = new DatagramSocket(4445)) {
                socket.setSoTimeout(0);
                byte[] buf = new byte[256];
                LocalHostPlusClientMod.LOGGER.info("[Discovery] Listening for LAN servers...");

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String data = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

                    // Format: MCHotspot:<uuid>:<port>
                    String[] parts = data.split(":");
                    if (parts.length == 3 && parts[0].equals("MCHotspot")) {
                        String uuid = parts[1];
                        String port = parts[2];
                        String address = packet.getAddress().getHostAddress();

                        addServerEntry(address, port);
                        LocalHostPlusClientMod.LOGGER.info("[Discovery] Found host UUID " + uuid + " at " + address + ":" + port);
                    }
                }
            } catch (Exception e) {
                LocalHostPlusClientMod.LOGGER.error("[Discovery] Listener stopped: ", e);
            }
        });
    }

    private static void addServerEntry(String address, String port) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof MultiplayerScreen screen) {
            ServerInfo info = new ServerInfo("Hotspot World", address + ":" + port, false);
            screen.getServerList().add(info, false);
            LocalHostPlusClientMod.LOGGER.info("[Discovery] Server found: " + address + ":" + port);
        }
    }
                }
