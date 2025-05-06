package naufal90.localhostplus;

import net.fabricmc.api.ModInitializer;
import naufal90.localhostplus.config.ModConfig;
import naufal90.localhostplus.network.Broadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalHostPlusMod implements ModInitializer {
    public static final String MOD_ID = "localhostplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[LocalHostPlus] Mod Initialized!");

        Dummy.forceInit();
        System.out.println(ModConfig.serverMotd);
        Broadcaster.stopBroadcast();
        PlayerUUIDManager.init();
        PlayerDataManager.init();
        // dummy call
        // Register your screen & networking
        //screen.HotspotScreen.register();
        //network.ClientDiscovery.init();
    }
}

