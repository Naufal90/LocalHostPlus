package naufal90.localhostplus.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import naufal90.localhostplus.network.OnlineHostFetcher;
import naufal90.localhostplus.network.OnlineWorldData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class OnlinePlayScreen extends Screen {

    private EntryListWidget worldListWidget;
    private List<OnlineWorldData> worlds;

    protected OnlinePlayScreen() {
        super(Text.of("Online Worlds"));
    }

    @Override
    protected void init() {
        super.init();

        worlds = OnlineHostFetcher.fetchHosts();
        worldListWidget = new EntryListWidget(this.client, this.width, this.height, 32, this.height - 32, 24);

        for (OnlineWorldData world : worlds) {
            worldListWidget.addEntry(new WorldEntry(world));
        }

        this.addSelectableChild(worldListWidget);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Refresh"), button -> {
            this.client.setScreen(new OnlinePlayScreen());
        }).position(this.width / 2 - 100, this.height - 28)
          .size(200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Back"), button -> {
            this.client.setScreen(new HotspotSettingsScreen(null));
        }).position(this.width / 2 - 100, this.height - 52)
          .size(200, 20).build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);
        worldListWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    class WorldEntry extends EntryListWidget.Entry<WorldEntry> {
        private final OnlineWorldData data;

        public WorldEntry(OnlineWorldData data) {
            this.data = data;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean hovered, float tickDelta) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.textRenderer.draw(matrices, "World: " + data.worldName, x + 4, y + 2, 0xFFFFFF);
            client.textRenderer.draw(matrices, "Host: " + data.ip + ":" + data.port, x + 4, y + 12, 0xAAAAAA);

            // Add connect button
            client.textRenderer.draw(matrices, "[Join]", x + 200, y + 6, 0x00FF00);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Simple check if "Join" area was clicked
            if (mouseX > 200 && mouseX < 250) {
                MinecraftClient.getInstance().openScreen(null);
                MinecraftClient.getInstance().joinMultiplayerServer(data.ip, data.port);
            }
            return true;
        }
    }
}
