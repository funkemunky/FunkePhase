package cc.funkemunky.funkephase.listener;

import cc.funkemunky.funkephase.FunkePhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent e) {
        FunkePhase.getInstance().hasAlertsOn.remove(e.getPlayer().getUniqueId());
    }

}
