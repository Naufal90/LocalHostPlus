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
import naufal90.localhostplus.network.OnlineHostPublisher;
import naufal90.localhostplus.utils.NetworkUtils;
import naufal90.localhostplus.utils.PlayerUUIDManager;
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

    int col1X = this.width / 4 - 75;     // Kolom kiri
    int col2X = this.width * 3 / 4 - 75; // Kolom kanan
    int y = this.height / 2 - 70;
    int spacingY = 24;

    // Baris 1
    commandToggle = new ToggleButtonWidget(col1X, y, 150, 20, "Enable Commands", ModConfig.allowCheats);
    this.addDrawableChild(commandToggle);

    onlineModeToggle = new ToggleButtonWidget(col2X, y, 150, 20, "Online Mode", ModConfig.onlineMode);
    this.addDrawableChild(onlineModeToggle);
    y += spacingY;

    // Baris 2
    pvpToggle = new ToggleButtonWidget(col1X, y, 150, 20, "PVP", ModConfig.allowPvp);
    this.addDrawableChild(pvpToggle);

    gameModeButton = CyclingButtonWidget.<GameMode>builder(GameMode::getSimpleTranslatableName)
        .values(GameMode.values())
        .initially(GameMode.byId(ModConfig.gameModeId))
        .build(col2X, y, 150, 20, Text.literal("Gamemode"));
    this.addDrawableChild(gameModeButton);
    y += spacingY;

    // Baris 3
    portField = new TextFieldWidget(textRenderer, col1X, y, 150, 20, Text.literal("Port"));
    portField.setText(String.valueOf(ModConfig.serverPort));
    this.addDrawableChild(portField);

    maxPlayersField = new TextFieldWidget(textRenderer, col2X, y, 150, 20, Text.literal("Max Players"));
    maxPlayersField.setText(String.valueOf(ModConfig.maxPlayers));
    this.addDrawableChild(maxPlayersField);
    y += spacingY;

    // Baris 4
    startStopButton = ButtonWidget.builder(
        Text.literal(hotspotActive ? "Stop Server" : "Start Server"),
        btn -> toggleHotspot()
    ).position(col1X, y).size(150, 20).build();
    this.addDrawableChild(startStopButton);

    this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), btn -> this.client.setScreen(parent))
        .position(col2X, y)
        .size(150, 20)
        .build()
    );

    this.addDrawableChild(ButtonWidget.builder(Text.of("Start Online World"), button -> {
    if (this.client.getServer() instanceof IntegratedServer) {
        try {
            String ip = NetworkUtils.getLocalIp();
            String username = this.client.getSession().getUsername();
            String uuid = PlayerUUIDManager.getOfflineUUID(username).toString();
            int port = ModConfig.serverPort;
            String worldName = this.client.getServer().getSaveProperties().getLevelName();
            String motd = ModConfig.serverMotd;
            int maxPlayers = ModConfig.maxPlayers;
            boolean onlineMode = ModConfig.onlineMode;

            OnlineHostPublisher.publish(username,uuid, ip, port, worldName, motd, maxPlayers, onlineMode);

            this.client.player.sendMessage(Text.of("[LocalHostPlus] Online world published at " + ip + ":" + port), false);
        } catch (Exception e) {
            this.client.player.sendMessage(Text.of("§cGagal mempublikasikan online world: " + e.getMessage()), false);
            e.printStackTrace();
        }
    } else {
        this.client.player.sendMessage(Text.of("§cServer belum berjalan. Mulai server terlebih dahulu."), false);
    }
}).position(this.width / 2 - 100, this.height - 80).size(200, 20).build());
}

    private boolean isWorldRunning() {
    return MinecraftClient.getInstance().getServer() != null;
    }
    
    private void toggleHotspot() {
    if (this.client.getServer() instanceof IntegratedServer server) {
        if (!hotspotActive) {
            try {
            // Ambil data dari UI lalu simpan ke ModConfig
            ModConfig.serverPort = parsePort(portField.getText());
            ModConfig.maxPlayers = Integer.parseInt(maxPlayersField.getText());
            ModConfig.gameModeId = gameModeButton.getValue().getId();
            ModConfig.allowPvp = pvpToggle.getValue();
            ModConfig.allowCheats = commandToggle.getValue();
            ModConfig.onlineMode = onlineModeToggle.getValue();

            // Jalankan server dengan konfigurasi
            server.setDefaultGameMode(gameModeButton.getValue());
            server.setPvpEnabled(ModConfig.allowPvp);
            server.getPlayerManager().setCheatsAllowed(ModConfig.allowCheats);
            server.setOnlineMode(ModConfig.onlineMode);
            server.setServerPort(ModConfig.serverPort);

            String username = this.client.getSession().getUsername();
                Broadcaster.startBroadcast(username, ModConfig.serverPort);
            hotspotActive = true;
                startStopButton.setMessage(Text.literal("Stop Server"));

                // Logging ke konsol
                System.out.println("[LocalHostPlus] Server berhasil dijalankan.");
                System.out.println("[LocalHostPlus] Port: " + ModConfig.serverPort);
                System.out.println("[LocalHostPlus] Max Players: " + ModConfig.maxPlayers);
                System.out.println("[LocalHostPlus] Gamemode: " + gameModeButton.getValue().getName());
                System.out.println("[LocalHostPlus] Online Mode: " + ModConfig.onlineMode);
                System.out.println("[LocalHostPlus] PVP: " + ModConfig.allowPvp);
                System.out.println("[LocalHostPlus] Cheats: " + ModConfig.allowCheats);
            
            // Kirim pesan ke pemain
            if (this.client.player != null) {
                try {
                    String ip = java.net.InetAddress.getLocalHost().getHostAddress();
                    this.client.player.sendMessage(Text.literal("[LocalHostPlus] Server aktif di " + ip + ":" + ModConfig.serverPort), false);
                } catch (Exception e) {
                    this.client.player.sendMessage(Text.literal("[LocalHostPlus] Server aktif di port " + ModConfig.serverPort), false);
                }
            }
            } catch (Exception e) {
                if (this.client.player != null) {
                    this.client.player.sendMessage(Text.literal("§cGagal memulai server: " + e.getMessage()), false);
                }
                System.err.println("[LocalHostPlus] Error saat memulai server: " + e.getMessage());
                return;
        }      
        } else {
            Broadcaster.stopBroadcast();
            hotspotActive = false;
            startStopButton.setMessage(Text.literal("Start Server"));

            System.out.println("[LocalHostPlus] Server dimatikan.");

            if (this.client.player != null) {
                this.client.player.sendMessage(Text.literal("[LocalHostPlus] Server dinonaktifkan."), false);
            }
        }
    } else {
        System.err.println("[LocalHostPlus] Gagal mengakses server: Bukan instance IntegratedServer.");
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
