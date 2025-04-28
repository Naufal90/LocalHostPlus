package naufal90.localhostplus.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HotspotServer {
    private static Thread broadcastThread;
    private static boolean running = false;

    public static void startBroadcast(String motd, int port) {
        if (running) return;
        running = true;
        broadcastThread = new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket();
                socket.setBroadcast(true);

                String message = "[MOTD]" + motd + "[/MOTD][AD]" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "[/AD]";
                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 4445);

                while (running) {
                    socket.send(packet);
                    Thread.sleep(1500);
                }
                socket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }, "LocalHostPlus-Broadcast");
        broadcastThread.start();
    }

    public static void stopBroadcast() {
        running = false;
    }
}
