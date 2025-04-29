package naufal90.localhostplus.screen;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import naufal90.localhostplus.screen.HotspotSettingsScreen;

public class HotspotScreen {
    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof GameMenuScreen gameMenuScreen) {
                // Tambah tombol "Open to Hotspot" di pause menu
                screen.addDrawableChild(
    ButtonWidget.builder(
        Text.literal("Hotspot Local Server"),
        button -> {
            client.setScreen(new HotspotSettingsScreen(screen));
        }
    ).position(10, 10).size(150, 20).build()
);
            }
        });
    }
}
