package naufal90.localhostplus.screen;

import naufal90.localhostplus.network.OnlineHostFetcher;
import naufal90.localhostplus.network.OnlineWorldData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class OnlinePlayScreen extends Screen {
    private WorldListWidget worldList;
    private List<OnlineWorldData> worlds;

    public OnlinePlayScreen() {
        super(Text.literal("Online Worlds"));
    }

    @Override
    protected void init() {
        super.init();

        this.worlds = OnlineHostFetcher.fetchHosts();
        this.worldList = new WorldListWidget(this.client, this.width, this.height, 40, this.height - 40, 24);

        for (OnlineWorldData world : worlds) {
            this.worldList.addEntry(new WorldEntry(world));
        }

        this.addSelectableChild(worldList);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Refresh"), btn -> {
            this.client.setScreen(new OnlinePlayScreen());
        }).position(this.width / 2 - 100, this.height - 28)
          .size(200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Back"), btn -> {
            this.client.setScreen(new HotspotSettingsScreen(null));
        }).position(this.width / 2 - 100, this.height - 52)
          .size(200, 20).build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);
        worldList.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private static class WorldListWidget extends ElementListWidget<WorldEntry> {
        public WorldListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
            super(client, width, height, top, bottom, itemHeight);
        }

        @Override
        protected int getScrollbarPositionX() {
            return this.width - 6;
        }

        @Override
        public int getRowWidth() {
            return this.width - 12;
        }
    }

    private static class WorldEntry extends ElementListWidget.Entry<WorldEntry> {
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
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            System.out.println("[LocalHostPlus] Connecting to " + data.ip + ":" + data.port);
            // Tambahkan metode join server-mu sendiri di sini
            return true;
        }
    }
}
