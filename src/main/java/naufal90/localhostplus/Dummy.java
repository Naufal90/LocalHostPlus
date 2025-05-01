package naufal90.localhostplus;

public class Dummy {
    public static void forceInit() {
        // Referensikan class dari client agar masuk ke remap
        System.out.println("Dummy init: Linking to ClientModInitializer...");
    }
}
