package naufal90.localhostplus.screen;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import naufal90.localhostplus.network.HotspotServer;

public class HotspotScreen {
    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof GameMenuScreen gameMenuScreen) {
                // Gunakan method addDrawableChild yang tersedia di GameMenuScreen
                gameMenuScreen.addDrawableChild(
                    ButtonWidget.builder(
                        Text.literal("Enable Hotspot Server"),
                        button -> {
                            HotspotServer.openToLan();
                            if (client.player != null) {
                                client.player.sendMessage(
                                    Text.literal("§aHotspot server started!"), 
                                    false
                                );
                            }
                        }
                    )
                    .position(10, 10)
                    .size(150, 20)
                    .build()
                );
            }
        });
    }
}
