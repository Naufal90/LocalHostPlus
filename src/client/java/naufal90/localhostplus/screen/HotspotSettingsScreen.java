package naufal90.localhostplus.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.world.GameMode;  // Ganti dari GameType ke GameMode
import naufal90.localhostplus.network.Broadcaster;
import naufal90.localhostplus.screen.ToggleButtonWidget;
import naufal90.localhostplus.config.ModConfig;

@Environment(EnvType.CLIENT)
public class HotspotSettingsScreen extends Screen {
    private final Screen parent;
    private ButtonWidget startStopButton;
    private boolean hotspotActive = false;
    private TextFieldWidget portField;
    private TextFieldWidget maxPlayersField;
    private CyclingButtonWidget<GameMode> gameModeButton;
    private ToggleButtonWidget pvpToggle;
    private ToggleButtonWidget commandToggle;
    private ToggleButtonWidget onlineModeToggle;

    public HotspotSettingsScreen(Screen parent) {
        super(Text.literal("LocalHostPlus Settings"));
        this.parent = parent;
    }

    @Override
protected void init() {
    this.hotspotActive = Broadcaster.isBroadcasting();
    
    int centerX = this.width / 2;
    int y = this.height / 2 - 60;

// ====================== PORT FIELD ======================
portField = new TextFieldWidget(
    this.textRenderer, 
    centerX - 75, 
    y, 
    150, 
    20, 
    Text.literal("Port")
);
portField.setText(String.valueOf(ModConfig.serverPort));
portField.setTooltip(Tooltip.of(
    Text.literal("Port server (default: 25565)\nGunakan port 1024-65535")
));
this.addDrawableChild(portField);
y += 24;

 // ====================== MAX PLAYERS FIELD ======================
  maxPlayersField = new TextFieldWidget(
    textRenderer, centerX - 75, y, 150, 20, Text.literal("Max Players")
);
maxPlayersField.setText(String.valueOf(ModConfig.maxPlayers));
maxPlayersField.setTooltip(Tooltip.of(Text.literal("Maksimal pemain yang bisa join (1-20)"))); // Jangan lupa tooltip!
maxPlayersField.setChangedListener(text -> {
    try {
        int newMax = Integer.parseInt(text);
        if (isWorldRunning() && newMax != ModConfig.maxPlayers) {
            maxPlayersField.setMessage(Text.literal("§cRestart world terlebih dahulu!"));
            return;
        }
        ModConfig.maxPlayers = MathHelper.clamp(newMax, 1, 20);
    } catch (NumberFormatException e) {
        maxPlayersField.setMessage(Text.literal("§cHarus angka antara 1-20!"));
    }
});
addDrawableChild(maxPlayersField);
y += 24;

// ====================== GAMEMODE BUTTON ======================
gameModeButton = this.addDrawableChild(
    CyclingButtonWidget.<GameMode>builder(GameMode::getSimpleTranslatableName)
        .values(GameMode.values())
        .initially(GameMode.byId(ModConfig.gamemodeId))
        .build(centerX - 75, y, 150, 20, Text.literal("Gamemode"))
);
gameModeButton.setTooltip(Tooltip.of(
    Text.literal("Atur mode permainan default\nSurvival/Creative/Adventure/Spectator")
));
y += 24;

// ====================== ONLINE MODE TOGGLE ======================
onlineModeToggle = new ToggleButtonWidget(
    centerX - 75, 
    y, 
    150, 
    20, 
    "Online Mode", 
    ModConfig.onlineMode
);
onlineModeToggle.setTooltip(Tooltip.of(Text.literal(
    "ON: Hanya pemain premium bisa join\n" +
    "OFF: Semua pemain bisa join (offline mode)"
)));
this.addDrawableChild(onlineModeToggle);
y += 24;

// ====================== PVP TOGGLE ======================
pvpToggle = new ToggleButtonWidget(
    centerX - 75, 
    y, 
    150, 
    20, 
    "PVP", 
    ModConfig.allowPvp
);
pvpToggle.setTooltip(Tooltip.of(Text.literal(
    "Aktifkan untuk izinkan PvP antar pemain\n" +
    "Nonaktifkan untuk mode damai"
)));
this.addDrawableChild(pvpToggle);
y += 24;

// ====================== COMMAND TOGGLE ======================
commandToggle = new ToggleButtonWidget(
    centerX - 75, 
    y, 
    150, 
    20, 
    "Enable Commands", 
    ModConfig.allowCheats
);
commandToggle.setTooltip(Tooltip.of(Text.literal(
    "Aktifkan untuk izinkan perintah cheat\n" +
    "Contoh: /gamemode, /give, dll"
)));
this.addDrawableChild(commandToggle);
y += 30;

// ====================== START/STOP BUTTON ======================
startStopButton = this.addDrawableChild(
    ButtonWidget.builder(
        Text.literal(hotspotActive ? "Stop Server" : "Start Server"), 
        btn -> toggleHotspot()
    )
    .position(centerX - 75, y)
    .size(150, 20)
    .tooltip(Tooltip.of(Text.literal(
        hotspotActive ? "Matikan server hotspot" : 
        "Nyalakan server dan broadcast ke jaringan lokal"
    )))
    .build()
);
y += 24;

// ====================== BACK BUTTON ======================
this.addDrawableChild(
    ButtonWidget.builder(Text.literal("Back"), btn -> this.client.setScreen(parent))
        .position(centerX - 75, y)
        .size(150, 20)
        .build()
);
}

    private boolean isWorldRunning() {
    return MinecraftClient.getInstance().getServer() != null;
    }
    
    private void toggleHotspot() {
    if (this.client.getServer() instanceof IntegratedServer server) {
        if (!hotspotActive) {
            // Ambil data dari UI lalu simpan ke ModConfig
            ModConfig.serverPort = parsePort(portField.getText());
            ModConfig.maxPlayers = maxPlayers;
            ModConfig.gamemodeId = gameModeButton.getValue().getId();
            ModConfig.allowPvp = pvpToggle.getValue();
            ModConfig.allowCheats = commandToggle.getValue();
            ModConfig.onlineMode = onlineModeToggle.getvalue();

            // Jalankan server dengan konfigurasi
            server.setDefaultGameMode(gameModeButton.getValue());
            server.setPvpEnabled(ModConfig.allowPvp);
            server.getPlayerManager().setCheatsAllowed(ModConfig.allowCheats);
            server.setOnlineMode(ModConfig.onlineMode);
            server.setServerPort(ModConfig.serverPort);

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
