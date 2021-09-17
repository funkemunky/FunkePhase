package cc.funkemunky.funkephase.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    private final Map<UUID, PlayerData> dataObjects = new HashMap<>();

    public DataManager() {
        //Creating objects for players who are already online on plugin startup
        for (Player player : Bukkit.getOnlinePlayers()) {
            createDataObject(player);
        }
    }

    public void createDataObject(Player player) {
        synchronized (dataObjects) {
            dataObjects.put(player.getUniqueId(), new PlayerData(player));
        }
    }

    public void removeDataObject(UUID uuid) {
        synchronized (dataObjects) {
            dataObjects.remove(uuid);
        }
    }

    public synchronized PlayerData getPlayerData(Player player) {
        return dataObjects.computeIfAbsent(player.getUniqueId(), key -> new PlayerData(player));
    }

}
