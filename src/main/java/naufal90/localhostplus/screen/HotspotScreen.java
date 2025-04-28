package naufal90.localhostplus.screen;

import naufal90.localhostplus.config.ModConfig;
import naufal90.localhostplus.network.HotspotServer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class HotspotScreen extends Screen {
    private final Screen parent;

    public HotspotScreen(Screen parent) {
        super(Text.literal("LocalHostPlus"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(Text.literal("Start Hosting"), button -> {
            HotspotServer.startBroadcast(ModConfig.serverMotd, ModConfig.serverPort);
            client.setScreen(parent);
        }).dimensions(this.width / 2 - 100, this.height / 2 - 24, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            client.setScreen(parent);
        }).dimensions(this.width / 2 - 100, this.height / 2 + 4, 200, 20).build());
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
