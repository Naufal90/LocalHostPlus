package naufal90.localhostplus.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import naufal90.localhostplus.network.Broadcaster;

@Environment(EnvType.CLIENT)
public class HotspotSettingsScreen extends Screen {
    private final Screen parent;
    private ButtonWidget startStopButton;
    private boolean hotspotActive = false;

    public HotspotSettingsScreen(Screen parent) {
        super(Text.literal("Hotspot Settings"));
        this.parent = parent;
    }

    @Override
protected void init() {
    this.hotspotActive = Broadcaster.isBroadcasting();

    startStopButton = this.addDrawableChild(
        ButtonWidget.builder(
            Text.literal(hotspotActive ? "Stop Server" : "Start Server"),
            button -> toggleHotspot()
        ).position(this.width / 2 - 75, this.height / 2 - 10)
        .size(150, 20).build()
    );

    // tombol Back
    this.addDrawableChild(
        ButtonWidget.builder(
            Text.literal("Back"),
            button -> this.client.setScreen(parent)
        ).position(this.width / 2 - 75, this.height / 2 + 20)
        .size(150, 20).build()
    );
}

    private void toggleHotspot() {
        if (this.client.getServer() instanceof IntegratedServer server) {
            if (!hotspotActive) {
                server.openToLan(null, false, 25565);
                Broadcaster.startBroadcast(25565);

                try {
    String localIp = java.net.InetAddress.getLocalHost().getHostAddress();
    this.client.player.sendMessage(
        Text.literal("[LocalHostPlus] Hotspot aktif di " + localIp + ":25565"),
        false
    );
} catch (Exception e) {
    this.client.player.sendMessage(
        Text.literal("[LocalHostPlus] Hotspot aktif di port 25565"),
        false
    );
                }
                hotspotActive = true;
                startStopButton.setMessage(Text.literal("Stop Server"));
            } else {
                Broadcaster.stopBroadcast();
                if (this.client.player != null) {
                    this.client.player.sendMessage(
                        Text.literal("[LocalHostPlus] Hotspot dinonaktifkan."),
                        false
                    );
                }
                hotspotActive = false;
                startStopButton.setMessage(Text.literal("Start Server"));
            }
        }
        
        this.client.setScreen(null); // Tutup kembali ke game
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
