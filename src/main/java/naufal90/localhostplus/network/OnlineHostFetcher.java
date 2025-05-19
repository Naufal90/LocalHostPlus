package naufal90.localhostplus.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class OnlineHostFetcher {

    private static final String API_URL = "https://api.naufal90.my.id/hosts"; // Ganti sesuai backend

    public static List<OnlineWorldData> fetchHosts() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            return new Gson().fromJson(reader, new TypeToken<List<OnlineWorldData>>(){}.getType());
        } catch (Exception e) {
            System.err.println("[LocalHostPlus] Failed to fetch host list: " + e.getMessage());
            return List.of();
        }
    }
}
