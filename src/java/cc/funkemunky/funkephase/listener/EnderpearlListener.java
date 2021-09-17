package cc.funkemunky.funkephase.listener;

import cc.funkemunky.api.utils.Materials;
import cc.funkemunky.funkephase.FunkePhase;
import cc.funkemunky.funkephase.data.PlayerData;
import cc.funkemunky.funkephase.util.FionaLocation;
import cc.funkemunky.funkephase.util.MiscUtils;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Comparator;
import java.util.List;

public class EnderpearlListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(PlayerTeleportEvent event) {
        if(event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            PlayerData data = FunkePhase.getInstance().getDataManager().getPlayerData(event.getPlayer());

            if(data != null) data.setEnderPearlTeleport(true);

            event.getTo().setY(event.getTo().getY() + 0.5f);

            if(FunkePhase.getInstance().epStuckProt && MiscUtils.isInSolidBlock(MiscUtils
                    .getPlayerBoxByLocation(event.getTo().toVector()), event.getTo().getWorld())) {
                double xMin = Math.min(event.getFrom().getX(), event.getTo().getX());
                double yMin = Math.min(event.getFrom().getY(), event.getTo().getY());
                double zMin = Math.min(event.getFrom().getZ(), event.getTo().getZ());
                double xMax = Math.max(event.getFrom().getX(), event.getTo().getX());
                double yMax = Math.max(event.getFrom().getY(), event.getTo().getY());
                double zMax = Math.max(event.getFrom().getZ(), event.getTo().getZ());

                List<FionaLocation> vectors = Lists.newArrayList();
                for (double x = xMin; x < xMax; x++) {
                    for (double y = yMin; y < yMax; y ++) {
                        for (double z = zMin; z < zMax; z ++) {
                            FionaLocation vector = new FionaLocation(x, y, z,
                                    event.getPlayer().getEyeLocation().getYaw(),
                                    event.getPlayer().getEyeLocation().getPitch());

                            vectors.add(vector);
                        }
                    }
                }

                vectors.sort(Comparator.comparing(vector -> vector.toVector().distance(event.getTo().toVector())));

                for (FionaLocation vector : vectors) {
                    Location location = vector.toLocation(event.getPlayer().getWorld());
                    if (!Materials.checkFlag(location.getBlock().getType(), Materials.SOLID)
                            && !Materials.checkFlag(location
                            .add(0, 1,0).getBlock().getType(), Materials.SOLID)) {
                        event.setTo(vector.toLocation(event.getPlayer().getWorld()));
                        break;
                    }
                }
            }
        }
    }
}
