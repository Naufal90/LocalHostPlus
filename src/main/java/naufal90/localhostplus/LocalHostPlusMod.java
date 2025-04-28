package naufal90.localhostplus;

import naufal90.localhostplus.network.ClientDiscovery;
import naufal90.localhostplus.network.HotspotServer;
import naufal90.localhostplus.screen.HotspotScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalHostPlusMod implements ClientModInitializer {
    public static final String MOD_ID = "localhostplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[LocalHostPlus] Mod Initialized!");
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
