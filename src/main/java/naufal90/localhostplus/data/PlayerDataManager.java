package naufal90.localhostplus.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Simpan inventory sebagai NbtCompound dan ubah jadi String
        NbtList inventoryNbt = player.getInventory().writeNbt(new NbtList());
        data.put("inventory", inventoryNbt.asString());

        // Simpan posisi
        Vec3d pos = player.getPos();
        data.put("position", new double[]{pos.x, pos.y, pos.z});

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
            String invStr = (String) data.get("inventory");
            NbtList invNbt = (NbtList) NbtIo.readCompressed(new ByteArrayInputStream(invStr.getBytes()));

            player.getInventory().readNbt(invNbt);

            // Teleport ke posisi
            List<Double> posList = (List<Double>) data.get("position");
            if (posList != null && posList.size() == 3) {
                player.requestTeleport(posList.get(0), posList.get(1), posList.get(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Text.literal("Gagal memuat data pemain."), false);
        }
    }
}
