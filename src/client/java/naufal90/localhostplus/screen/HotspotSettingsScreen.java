package naufal90.localhostplus.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.world.GameMode;  // Ganti dari GameType ke GameMode
import naufal90.localhostplus.network.Broadcaster;
import naufal90.localhostplus.screen.ToggleButtonWidget;

@Environment(EnvType.CLIENT)
public class HotspotSettingsScreen extends Screen {
    private final Screen parent;
    private ButtonWidget startStopButton;
    private boolean hotspotActive = false;
    private TextFieldWidget portField;
    private CyclingButtonWidget<GameType> gameModeButton;
    private ToggleButtonWidget pvpToggle;
    private ToggleButtonWidget commandToggle;

    public HotspotSettingsScreen(Screen parent) {
        super(Text.literal("LocalHostPlus Settings"));
        this.parent = parent;
    }

    @Override
protected void init() {
    this.hotspotActive = Broadcaster.isBroadcasting();

    int centerX = this.width / 2;
    int y = this.height / 2 - 60;

    // PORT field
    portField = new TextFieldWidget(this.textRenderer, centerX - 75, y, 150, 20, Text.literal("Port"));
    portField.setText(String.valueOf(ModConfig.serverPort));
    this.addDrawableChild(portField);
    y += 24;

    // GAMEMODE button
    gameModeButton = this.addDrawableChild(
        CyclingButtonWidget.builder(GameType::getShortDisplayName)
            .values(GameType.SURVIVAL, GameType.CREATIVE, GameType.ADVENTURE, GameType.SPECTATOR)
            .initially(GameType.byId(ModConfig.gamemodeId))
            .build(centerX - 75, y, 150, 20, Text.literal("Gamemode"))
    );
    y += 24;

    // PVP toggle
    pvpToggle = new ToggleButtonWidget(centerX - 75, y, 150, 20, "PVP", ModConfig.allowPvp);
    this.addDrawableChild(pvpToggle);
    y += 24;

    // COMMAND toggle
    commandToggle = new ToggleButtonWidget(centerX - 75, y, 150, 20, "Enable Commands", ModConfig.allowCheats);
    this.addDrawableChild(commandToggle);
    y += 30;

    // START / STOP
    startStopButton = this.addDrawableChild(
        ButtonWidget.builder(Text.literal(hotspotActive ? "Stop Server" : "Start Server"), btn -> toggleHotspot())
            .position(centerX - 75, y)
            .size(150, 20)
            .build()
    );
    y += 24;

    // BACK
    this.addDrawableChild(
        ButtonWidget.builder(Text.literal("Back"), btn -> this.client.setScreen(parent))
            .position(centerX - 75, y)
            .size(150, 20)
            .build()
    );
}

    private void toggleHotspot() {
    if (this.client.getServer() instanceof IntegratedServer server) {
        if (!hotspotActive) {
            // Ambil data dari UI lalu simpan ke ModConfig
            ModConfig.serverPort = parsePort(portField.getText());
            ModConfig.gamemodeId = gameModeButton.getValue().getId();
            ModConfig.allowPvp = pvpToggle.getValue();
            ModConfig.allowCheats = commandToggle.getValue();

            // Jalankan server dengan konfigurasi
            server.setDefaultGameType(gameModeButton.getValue());
            server.setPvpAllowed(ModConfig.allowPvp);
            server.getPlayerList().setAllowCheatsForAllPlayers(ModConfig.allowCheats);
            server.setUsesAuthentication(false); // misal untuk LAN
            server.openToLan(null, ModConfig.allowCheats, ModConfig.serverPort);

            Broadcaster.startBroadcast(ModConfig.serverPort);

            // Kirim pesan ke pemain
            if (this.client.player != null) {
                try {
                    String ip = java.net.InetAddress.getLocalHost().getHostAddress();
                    this.client.player.sendMessage(Text.literal("[LocalHostPlus] Server aktif di " + ip + ":" + ModConfig.serverPort), false);
                } catch (Exception e) {
                    this.client.player.sendMessage(Text.literal("[LocalHostPlus] Server aktif di port " + ModConfig.serverPort), false);
                }
            }

            hotspotActive = true;
            startStopButton.setMessage(Text.literal("Stop Server"));
        } else {
            Broadcaster.stopBroadcast();
            if (this.client.player != null) {
                this.client.player.sendMessage(Text.literal("[LocalHostPlus] Server dinonaktifkan."), false);
            }
            hotspotActive = false;
            startStopButton.setMessage(Text.literal("Start Server"));
        }
    }

    this.client.setScreen(null); // Kembali ke game
}

private int parsePort(String text) {
    try {
        return Integer.parseInt(text);
    } catch (NumberFormatException e) {
        return 25565; // fallback
    }
}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
