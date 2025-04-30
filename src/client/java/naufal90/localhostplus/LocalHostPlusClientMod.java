// src/client/java/naufal90/localhostplus/LocalHostPlusClientMod.java

package naufal90.localhostplus;

//import naufal90.localhostplus.screen.HotspotScreen;
import naufal90.localhostplus.network.ClientDiscovery;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalHostPlusClientMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("localhostplus-client");
    
    @Override
    public void onInitializeClient() {
        //HotspotScreen.register();
        ClientDiscovery.init();
        //new HotspotSettingsScreen(null);
    }
}
