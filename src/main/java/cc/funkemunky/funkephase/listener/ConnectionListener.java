package cc.funkemunky.funkephase.listener;

import cc.funkemunky.funkephase.FunkePhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        FunkePhase.INSTANCE.getDataManager().createDataObject(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        FunkePhase.INSTANCE.getDataManager().removeDataObject(e.getPlayer().getUniqueId());
        FunkePhase.INSTANCE.getPlayersWithAlerts().remove(e.getPlayer().getUniqueId());
    }

}
