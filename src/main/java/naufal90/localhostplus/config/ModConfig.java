package naufal90.localhostplus.config;

public class ModConfig {
    public static boolean autoBroadcast = true;
    public static int serverPort = 25565;
    public static String serverMotd = "LocalHostPlus Server!";
    public static boolean pvp = true;
    public static boolean allowCommands = true;
    public static int maxPlayers = 8;
    public static String gameMode = "survival"; // "creative", "adventure", etc.

    public static void saveConfig() {
        // TODO: optional, for saving to file
    }
}
