package naufal90.localhostplus.network;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnlineHostPublisher {

    private static final String API_URL = "https://api.naufal90.my.id/hosts";
    // Optimization: Use a single-thread executor for publishing to ensure order and offload from main thread
    private static final ExecutorService publisherExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "LocalHostPlus-Publisher");
        thread.setDaemon(true);
        return thread;
    });

    /**
     * Publishes host information to the central registry.
     * Optimization: Runs asynchronously to avoid blocking the game's UI thread.
     */
    public static void publish(String uuid, String username, String ip, int port, String worldName, String motd, int maxPlayers, boolean onlineMode) {
        publisherExecutor.execute(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setDoOutput(true);

                String json = String.format("""
        {
            "uuid": "%s",
            "username": "%s",
            "ip": "%s",
            "port": %d,
            "worldName": "%s",
            "motd": "%s",
            "maxPlayers": %d,
            "onlineMode": %b
        }
        """, uuid, username, ip, port, worldName, motd, maxPlayers, onlineMode);

                System.out.println("[LocalHostPlus] JSON body:");
                System.out.println(json);

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes(StandardCharsets.UTF_8);
                    os.write(input);
                }

                int responseCode = con.getResponseCode();
                if (responseCode != 200 && responseCode != 201) {
                    System.err.println("[LocalHostPlus] Server response code: " + responseCode);
                    try (var is = con.getErrorStream()) {
                        if (is != null) {
                            String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                            System.err.println("[LocalHostPlus] Response body: " + response);
                        }
                    }
                } else {
                    System.out.println("[LocalHostPlus] Successfully published host to online registry.");
                }

            } catch (Exception e) {
                System.err.println("[LocalHostPlus] Failed to publish host: " + e.getMessage());
            }
        });
    }
}
