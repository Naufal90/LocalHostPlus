package naufal90.localhostplus.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;
import naufal90.localhostplus.network.OnlineHostFetcher;
import naufal90.localhostplus.network.OnlineWorldData;

import java.util.Collections;
import java.util.List;

public class OnlinePlayScreen extends Screen {
    private OnlineWorldListWidget worldList;
    private List<OnlineWorldData> worlds;

    public OnlinePlayScreen() {
        super(Text.literal("Online Worlds"));
    }

    @Override
    protected void init() {
        this.worlds = OnlineHostFetcher.fetchHosts();

        this.worldList = new OnlineWorldListWidget(this.client, this.width, this.height, 40, this.height - 60, 24);
        for (OnlineWorldData world : worlds) {
            this.worldList.addWorldEntry(this.worldList.new WorldEntry(world));
        }
        this.addSelectableChild(this.worldList);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Refresh"), button -> {
            this.client.setScreen(new OnlinePlayScreen());
        }).position(this.width / 2 - 100, this.height - 50).size(200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Back"), button -> {
            this.client.setScreen(new HotspotSettingsScreen(null));
        }).position(this.width / 2 - 100, this.height - 25).size(200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int titleWidth = this.textRenderer.getWidth(this.title);
        context.drawText(this.textRenderer, this.title, (this.width - titleWidth) / 2, 12, 0xFFFFFF, false);
        this.worldList.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    public static class OnlineWorldListWidget extends EntryListWidget<OnlineWorldListWidget.WorldEntry> {
        public OnlineWorldListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
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

        @Override
protected void appendNarrations(NarrationMessageBuilder builder) {
    // Kosongkan jika tidak menggunakan narasi
}

        public static class WorldEntry extends EntryListWidget.Entry<WorldEntry> {
            private final OnlineWorldData data;

            public WorldEntry(OnlineWorldData data) {
                this.data = data;
            }
            public void addWorldEntry(WorldEntry entry) {
                this.addEntry(entry);
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                               int mouseX, int mouseY, boolean hovered, float delta) {
                TextRenderer tr = MinecraftClient.getInstance().textRenderer;
                context.drawText(tr, "World: " + data.worldName, x + 4, y + 2, 0xFFFFFF, false);
                context.drawText(tr, "Host: " + data.ip + ":" + data.port, x + 4, y + 12, 0xAAAAAA, false);
            }

            @Override
public List<? extends Selectable> selectableChildren() {
    return Collections.emptyList();
}

@Override
public List<? extends Element> children() {
    return Collections.emptyList();
}

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                System.out.println("[LocalHostPlus] Connecting to " + data.ip + ":" + data.port);
                // TODO: Tambahkan koneksi ke server
                return true;
            }
        }
    }
}
