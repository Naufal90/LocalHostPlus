package naufal90.localhostplus;

import naufal90.localhostplus.network.ClientDiscovery;
import naufal90.localhostplus.network.HotspotServer;
import naufal90.localhostplus.screen.HotspotScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.TitleScreen;

public class LocalHostPlusMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen) {
                client.execute(() -> {
                    client.setScreen(new HotspotScreen(screen));
                });
            }
        });

        // Start discovery listener
        ClientDiscovery.startListening();
    }
}
