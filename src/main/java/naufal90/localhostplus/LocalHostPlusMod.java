package naufal90.localhostplus;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalHostPlusMod implements ModInitializer {
    public static final String MOD_ID = "localhostplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[LocalHostPlus] Mod Initialized!");

        // Register your screen & networking
        //screen.HotspotScreen.register();
        //network.ClientDiscovery.init();
    }
}
