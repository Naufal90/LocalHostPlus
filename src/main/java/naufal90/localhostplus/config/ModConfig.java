package naufal90.localhostplus.config;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    public static boolean autoBroadcast = true;
    public static int serverPort = 25565;
    public static String serverMotd = "LocalHostPlus Server!";
    public static boolean allowPvp = true;
    public static boolean onlineMode = false;
    public static boolean allowCheats = true;
    public static int maxPlayers = 8;
    public static int gameModeId = 0; // default ke Survival
    public static String worldName = "MyWorld"; // Tambahan

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter("config/localhostplus.json")) {
            new Gson().toJson(new ConfigData(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        File file = new File("config/localhostplus.json");
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                ConfigData data = new Gson().fromJson(reader, ConfigData.class);
                autoBroadcast = data.autoBroadcast;
                serverPort = data.serverPort;
                serverMotd = data.serverMotd;
                allowPvp = data.allowPvp;
                onlineMode = data.onlineMode;
                allowCheats = data.allowCheats;
                maxPlayers = data.maxPlayers;
                gameModeId = data.gameModeId;
                worldName = data.worldName; // Tambahan
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Wrapper untuk serialisasi
    private static class ConfigData {
        boolean autoBroadcast;
        int serverPort;
        String serverMotd;
        boolean allowPvp;
        boolean onlineMode;
        boolean allowCheats;
        int maxPlayers;
        int gameModeId;
        String worldName; // Tambahan

        public ConfigData() {
            this.autoBroadcast = ModConfig.autoBroadcast;
            this.serverPort = ModConfig.serverPort;
            this.serverMotd = ModConfig.serverMotd;
            this.allowPvp = ModConfig.allowPvp;
            this.onlineMode = ModConfig.onlineMode;
            this.allowCheats = ModConfig.allowCheats;
            this.maxPlayers = ModConfig.maxPlayers;
            this.gameModeId = ModConfig.gameModeId;
            this.worldName = ModConfig.worldName; // Tambahan
        }
    }
}
