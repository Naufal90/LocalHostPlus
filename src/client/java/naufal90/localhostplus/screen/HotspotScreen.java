package naufal90.localhostplus.screen;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import naufal90.localhostplus.network.HotspotServer;

public class HotspotScreen {
    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
    if (screen instanceof GameMenuScreen gameMenuScreen) {
        gameMenuScreen.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Hotspot Local Server"),
                button -> {
                    client.setScreen(new TitleScreen()); // Ganti ke screen tujuanmu nanti
                }
            ).position(10, 10).size(150, 20).build()
        );
    }
});
    }
}
