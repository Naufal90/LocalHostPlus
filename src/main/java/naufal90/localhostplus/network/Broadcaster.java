package naufal90.localhostplus.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import naufal90.localhostplus.utils.NetworkUtils;

public class Broadcaster {
    private static Thread thread;
    private static volatile boolean running = false;

    /**
     * Starts the LAN broadcast.
     * Optimization: Moved object allocations outside the loop to reduce GC pressure.
     * Added thread safety and better shutdown handling.
     */
    public static synchronized void startBroadcast(String uuid, int port) {
        if (running) return;
        running = true;

        thread = new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(true);
                InetAddress address = NetworkUtils.getBroadcastAddress();

                // Optimization: Pre-allocate the message and packet outside the loop
                // Format: MCHotspot:<uuid>:<port>
                String message = "MCHotspot:" + uuid + ":" + port;
                byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4445);

                while (running && !Thread.currentThread().isInterrupted()) {
                    socket.send(packet);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                // Thread was interrupted for shutdown, exit gracefully
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running = false;
            }
        }, "LocalHostPlus-Broadcaster");

        thread.start();
    }

    public static synchronized void stopBroadcast() {
        running = false;
        if (thread != null && thread.isAlive()) {
            thread.interrupt(); // Wake up from Thread.sleep(1000) immediately
            try {
                thread.join(500); // Wait for the thread to finish
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
        thread = null;
    }

    public static boolean isBroadcasting() {
        return running;
    }
}
