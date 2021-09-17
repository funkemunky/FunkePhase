package cc.funkemunky.funkephase.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager implements Listener {

    /**
     * Player Object Stufff
     **/
    private final Map<UUID, PlayerData> dataObjects = new ConcurrentHashMap<>();

    public DataManager() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            createDataObject(player);
        }
    }

    public void createDataObject(Player player) {
        dataObjects.put(player.getUniqueId(), new PlayerData(player));
    }

    public void removeDataObject(UUID uuid) {
        dataObjects.remove(uuid);
    }

    public PlayerData getPlayerData(Player player) {
        return dataObjects.computeIfAbsent(player.getUniqueId(), key -> new PlayerData(player));
    }

    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        createDataObject(event.getPlayer());
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        removeDataObject(event.getPlayer().getUniqueId());
    }

}
