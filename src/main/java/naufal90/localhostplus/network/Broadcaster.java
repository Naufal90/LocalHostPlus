package naufal90.localhostplus.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Broadcaster {
    private static Thread thread;
    private static boolean running = false;

    public static void startBroadcast(int port) {
        running = true;
        thread = new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(true);
                InetAddress address = InetAddress.getByName("255.255.255.255");

                while (running) {
                    String message = "MCHotspot:" + port;
                    byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4445);
                    socket.send(packet);

                    Thread.sleep(1000); // Broadcast every 1 second
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public static void stopBroadcast() {
    running = false;
    if (thread != null && thread.isAlive()) {
        try {
            thread.join(500); // tunggu 0.5 detik
        } catch (InterruptedException ignored) {}
    }
}
