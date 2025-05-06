package naufal90.localhostplus.config;

public class ModConfig {
    public static boolean autoBroadcast = true;
    public static int serverPort = 25565;
    public static String serverMotd = "LocalHostPlus Server!";
    public static boolean allowPvp = true;
    public static boolean onlineMode = false;
    public static boolean allowCheats = true;
    public static int maxPlayers = 8;
    public static String gameModeId = "survival"; // "creative", "adventure", etc.

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter("config/localhostplus.json")) {
            new Gson().toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load dari file
    public static void loadConfig() {
        File file = new File("config/localhostplus.json");
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                ModConfig config = new Gson().fromJson(reader, ModConfig.class);
                maxPlayers = config.maxPlayers;
                offlineMode = config.offlineMode;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
