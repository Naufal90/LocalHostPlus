package naufal90.localhostplus.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;

public class HotspotSettingsScreen extends Screen {
    private final Screen parent;
    private ButtonWidget startButton;

    public HotspotSettingsScreen(Screen parent) {
        super(Text.literal("Hotspot Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Tombol Start Hotspot
        startButton = this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Start Open to Hotspot"),
                button -> {
                    // Mulai Hotspot disini
                    startHotspot()
                }
            ).position(this.width / 2 - 75, this.height / 2 - 10)
            .size(150, 20).build()
        );

        // Tombol Kembali
        this.addDrawable(
            ButtonWidget.builder(
                Text.literal("Back"),
                button -> {
                    this.client.setScreen(parent);
                }
            ).position(this.width / 2 - 75, this.height / 2 + 20)
            .size(150, 20).build()
        );
    }

    private void startHotspot() {
    if (this.client.getServer() instanceof IntegratedServer server) {
        server.openToLan(null, false, 25565); // null = pakai gamemode yang lagi dipakai, false = offline mode
        if (this.client.player != null) {
            this.client.player.sendMessage(
                Text.literal("[Hotspot] Server berhasil dimulai di jaringan lokal!"),
                false
            );
        }
    }
    // Tutup screen kembali ke game
    this.client.setScreen(null);
}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
