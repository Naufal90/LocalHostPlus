package naufal90.localhostplus.data;

import com.google.gson.Gson;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {
    private static final String DATA_DIR = "config/localhostplus/playerdata/";
    private static final Gson gson = new Gson();

    public static void savePlayerData(Player player) {
        Map<String, Object> data = new HashMap<>();
        data.put("inventory", player.getInventory().save(new ListTag()));
        data.put("position", new double[]{player.getX(), player.getY(), player.getZ()});

        new File(DATA_DIR).mkdirs(); // Buat folder jika belum ada
        File file = new File(DATA_DIR + player.getUUID() + ".json");
        
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPlayerData(Player player) {
        File file = new File(DATA_DIR + player.getUUID() + ".json");
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            Map<String, Object> data = gson.fromJson(reader, Map.class);
            player.getInventory().load((ListTag) data.get("inventory"));
            double[] pos = (double[]) data.get("position");
            player.teleportTo(pos[0], pos[1], pos[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        event.getWorld().getPlayers().forEach(PlayerDataManager::loadPlayerData);
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        event.getWorld().getPlayers().forEach(PlayerDataManager::savePlayerData);
    }
}
