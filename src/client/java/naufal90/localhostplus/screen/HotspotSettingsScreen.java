package naufal90.localhostplus.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public class HotspotSettingsScreen extends Screen {
    private final Screen parent;

    public HotspotSettingsScreen(Screen parent) {
        super(Text.literal("Hotspot Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Start Open to Hotspot"),
                button -> startHotspot()
            ).position(this.width / 2 - 75, this.height / 2 - 10)
            .size(150, 20).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Back"),
                button -> this.client.setScreen(parent)
            ).position(this.width / 2 - 75, this.height / 2 + 20)
            .size(150, 20).build()
        );
    }

    private void startHotspot() {
        if (this.client.getServer() instanceof IntegratedServer) {
    IntegratedServer server = (IntegratedServer) this.client.getServer();
    server.openToLan(null, false, 25565);
            if (this.client.player != null) {
                this.client.player.sendMessage(
                    Text.literal("[Hotspot] Server berhasil dimulai di jaringan lokal!"),
                    false
                );
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
