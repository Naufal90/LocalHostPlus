package naufal90.localhostplus.screen;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import naufal90.localhostplus.network.HotspotServer;

public class HotspotScreen {
    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof GameMenuScreen) {
                // 1. Convert ke Screen untuk akses addDrawableChild
                Screen baseScreen = (Screen) screen;
                
                // 2. Buat tombol dengan action yang benar
                baseScreen.addDrawableChild(
                    ButtonWidget.builder(
                        Text.literal("Enable Hotspot Server"),
                        button -> {
                            // 3. Panggil fungsi openToLan dari HotspotServer
                            HotspotServer.openToLan();
                            
                            // 4. Beri feedback ke player
                            if (client.player != null) {
                                client.player.sendMessage(Text.literal("§aHotspot server started!"), false);
                            }
                        }
                    )
                    .position(10, 10) // Posisi di pojok kiri atas
                    .size(150, 20)
                    .build()
                );
            }
        });
    }
}
