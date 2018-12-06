package cc.funkemunky.funkephase.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.ArrayList;
import java.util.List;

public class DataManager implements Listener {

    /**
     * Player Object Stufff
     **/
    private final List<PlayerData> dataObjects;

    public DataManager() {
        dataObjects = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            createDataObject(player);
        }

    }

    public void createDataObject(Player player) {
        dataObjects.add(new PlayerData(player));
    }

    public void removeDataObject(PlayerData dataObject) {
        dataObjects.remove(dataObject);
    }

    public PlayerData getPlayerData(Player player) {
        for (PlayerData data : dataObjects) {
            if (data.player == player) return data;
        }
        return null;
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
