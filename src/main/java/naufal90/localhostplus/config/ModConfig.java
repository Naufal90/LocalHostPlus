package naufal90.localhostplus.config;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    // Config Values
    public static boolean autoBroadcast = true;
    public static int serverPort = 25565;
    public static String serverMotd = "LocalHostPlus Server!";
    public static boolean allowPvp = true;
    public static boolean onlineMode = false;
    public static boolean allowCheats = true;
    public static int maxPlayers = 8;
    public static String gameModeId = "Survival";

    // ========== Masalah yang Diperbaiki ==========
    // 1. Method saveConfig() tidak bisa pakai 'this' karena static
    // 2. offlineMode tidak ada di class (seharusnya onlineMode)
    // 3. Tidak ada handling untuk semua variabel config saat load

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter("config/localhostplus.json")) {
            new Gson().toJson(new ConfigData(), writer); // Gunakan wrapper class
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        File file = new File("config/localhostplus.json");
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                ConfigData data = new Gson().fromJson(reader, ConfigData.class);
                // Update semua nilai
                autoBroadcast = data.autoBroadcast;
                serverPort = data.serverPort;
                serverMotd = data.serverMotd;
                allowPvp = data.allowPvp;
                onlineMode = data.onlineMode;
                allowCheats = data.allowCheats;
                maxPlayers = data.maxPlayers;
                gameModeId = data.gameModeId;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ========== Wrapper Class untuk Serialisasi ==========
    private static class ConfigData {
        boolean autoBroadcast;
        int serverPort;
        String serverMotd;
        boolean allowPvp;
        boolean onlineMode;
        boolean allowCheats;
        int maxPlayers;
        String gameModeId;

        public ConfigData() {
            this.autoBroadcast = ModConfig.autoBroadcast;
            this.serverPort = ModConfig.serverPort;
            this.serverMotd = ModConfig.serverMotd;
            this.allowPvp = ModConfig.allowPvp;
            this.onlineMode = ModConfig.onlineMode;
            this.allowCheats = ModConfig.allowCheats;
            this.maxPlayers = ModConfig.maxPlayers;
            this.gameModeId = ModConfig.gameModeId;
        }
    }
}
