package naufal90.localhostplus.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientDiscovery {
    private static Thread listenThread;
    private static boolean running = false;

    public static void startListening() {
        if (running) return;
        running = true;

        listenThread = new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket(4445);
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                while (running) {
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());

                    if (message.contains("[MOTD]") && message.contains("[/MOTD]") && message.contains("[AD]") && message.contains("[/AD]")) {
                        String motd = message.split("\MOTD\")[1].split("\/MOTD\")[0];
                        String addressPort = message.split("\AD\")[1].split("\/AD\")[0];
                        String address = addressPort.split(":")[0];
                        int port = Integer.parseInt(addressPort.split(":")[1]);

                        System.out.println("Discovered server: " + motd + " at " + address + ":" + port);
                        // TODO: Integrasikan ke Multiplayer screen nanti
                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "LocalHostPlus-Discovery");
        listenThread.start();
    }

    public static void stopListening() {
        running = false;
    }
}
