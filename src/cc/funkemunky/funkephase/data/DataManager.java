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

    public void removeDataObject(PlayerData dataObject) {
        dataObjects.remove(dataObject);
    }

    public PlayerData getPlayerData(Player player) {
        if(!dataObjects.containsKey(player.getUniqueId())) {
           createDataObject(player);
           return null;
        }
        return dataObjects.get(player.getUniqueId());
    }

    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        createDataObject(event.getPlayer());

        //event.getPlayer().sendMessage("test");
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        PlayerData data = getPlayerData(event.getPlayer());

        if (data != null) {
            removeDataObject(data);
        }
    }

}
