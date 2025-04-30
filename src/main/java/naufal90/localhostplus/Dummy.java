package naufal90.localhostplus;

public class Dummy {
    public static void forceInit() {
        // Referensikan class dari client agar masuk ke remap
        System.out.println("Dummy init: Linking to ClientModInitializer...");
        System.out.println(HotspotScreen.class);
        System.out.println(HotspotSettingsScreen.class);
        System.out.println(ClientDiscovery.class);
    }
}
