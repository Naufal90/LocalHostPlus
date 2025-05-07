package naufal90.localhostplus.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.*;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

public class PlayerDataManager {
    private static final String DATA_DIR = "config/localhostplus/playerdata/";
    private static final Gson gson = new Gson();

    public static void init() {
        // Simpan data saat player disconnect
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            savePlayerData(handler.getPlayer());
        });

        // Anda bisa menambahkan load data di join event jika diperlukan
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            loadPlayerData(handler.getPlayer());
        });

        // Buat folder saat server mulai jika belum ada
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            new File(DATA_DIR).mkdirs();
        });
    }

    public static void savePlayerData(ServerPlayerEntity player) {
        Map<String, Object> data = new HashMap<>();
        try {
        // Simpan inventory sebagai NbtList (pakai NbtIo untuk encode)
        NbtList invList = player.getInventory().writeNbt(new NbtList());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NbtIo.writeCompressed(new NbtCompound() {{
            put("inventory", invList);
        }}, out);
        String encoded = Base64.getEncoder().encodeToString(out.toByteArray());
        data.put("inventory", encoded);
        
        // Simpan posisi
        Vec3d pos = player.getPos();
        data.put("position", new double[]{pos.x, pos.y, pos.z});

        // Tambahan data player
        data.put("health", player.getHealth());
        data.put("foodLevel", player.getHungerManager().getFoodLevel());
        data.put("saturation", player.getHungerManager().getSaturationLevel());
        data.put("experience", player.experienceLevel);
        data.put("expProgress", player.experienceProgress);
        
        File file = new File(DATA_DIR + player.getUuidAsString() + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadPlayerData(ServerPlayerEntity player) {
        File file = new File(DATA_DIR + player.getUuidAsString() + ".json");
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = gson.fromJson(reader, type);

            // Load inventory dari string NBT
           String encoded = (String) data.get("inventory");
            byte[] bytes = Base64.getDecoder().decode(encoded);
            NbtCompound wrapper = NbtIo.readCompressed(new ByteArrayInputStream(bytes));
            NbtList invList = wrapper.getList("inventory", NbtElement.COMPOUND_TYPE);
            player.getInventory().readNbt(invList);
            
            // Teleport ke posisi
            List<?> posListRaw = (List<?>) data.get("position");
            if (posListRaw != null && posListRaw.size() == 3) {
                try {
                    double x = ((Number) posListRaw.get(0)).doubleValue();
                    double y = ((Number) posListRaw.get(1)).doubleValue();
                    double z = ((Number) posListRaw.get(2)).doubleValue();
                    player.requestTeleport(x, y, z);
                    // Pulihkan status player
                    if (data.containsKey("health")) {
                        player.setHealth(((Number) data.get("health")).floatValue());
                    }
                    if (data.containsKey("foodLevel") && data.containsKey("saturation")) {
                        player.getHungerManager().setFoodLevel(((Number) data.get("foodLevel")).intValue());
                        player.getHungerManager().setSaturationLevel(((Number) data.get("saturation")).floatValue());
                    }
                    if (data.containsKey("experience") && data.containsKey("expProgress")) {
                        player.experienceLevel = ((Number) data.get("experience")).intValue();
                        player.experienceProgress = ((Number) data.get("expProgress")).floatValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    player.sendMessage(Text.literal("Gagal memuat data pemain."), false);
                }
            }
        }
    }
}
    
