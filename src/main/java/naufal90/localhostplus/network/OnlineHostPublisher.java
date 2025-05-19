package naufal90.localhostplus.network;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OnlineHostPublisher {

    private static final String API_URL = "https://api.naufal90.my.id/hosts"; // Ganti sesuai backend

    public static void publish(String uuid, String ip, int port, String worldName, String motd, int maxPlayers) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String json = String.format("""
                {
                    "uuid": "%s",
                    "ip": "%s",
                    "port": %d,
                    "worldName": "%s",
                    "motd": "%s",
                    "maxPlayers": %d
                }
                """, uuid, ip, port, worldName, motd, maxPlayers);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            con.getInputStream().close(); // Pastikan respons dibaca agar request terkirim
        } catch (Exception e) {
            System.err.println("[LocalHostPlus] Failed to publish host: " + e.getMessage());
        }
    }
}
