package cc.funkemunky.funkephase.listener;

import cc.funkemunky.funkephase.FunkePhase;
import cc.funkemunky.funkephase.data.PlayerData;
import cc.funkemunky.funkephase.util.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class PhaseListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPhase(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        PlayerData data = FunkePhase.getInstance().getDataManager().getPlayerData(player);

        if (player.getAllowFlight()
                || player.getVehicle() != null
                || !FunkePhase.getInstance().toggled
                || MathUtils.elapsed(data.lastDoorSwing) < 500) {
            return;
        }

        if (e.getFrom().distance(e.getTo()) > 10) {
            e.setTo(e.getFrom());
            return;
        }

        float minX = (float) Math.min(e.getFrom().getX(), e.getTo().getX()), minY = (float) Math.min(e.getFrom().getY(), e.getTo().getY()), minZ = (float) Math.min(e.getFrom().getZ(), e.getTo().getZ()),
                maxX = (float) Math.max(e.getFrom().getX(), e.getTo().getX()), maxY = (float) Math.max(e.getFrom().getY(), e.getTo().getY()), maxZ = (float) Math.max(e.getFrom().getZ(), e.getTo().getZ());

        Object box = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ).add(0f, 0f, 0f, 0f, 1.8f, 0f).toAxisAlignedBB();

        if (ReflectionsUtil.getCollidingBlocks(e.getPlayer(), box).stream().anyMatch(aabb -> !FunkePhase.getInstance().getExcludedBlocks().contains(ReflectionsUtil.toBoundingBox(aabb).getMinimum().toLocation(data.player.getWorld()).getBlock().getType()))) {
            Location setback = findSetback(data);

            if(setback != null) {
                setback.setPitch(e.getTo().getPitch());
                setback.setYaw(e.getTo().getYaw());
            }
            e.setTo(setback != null ? setback : e.getFrom());
            FunkePhase.getInstance().alert(e.getPlayer());
            //e.getPlayer().sendMessage(ChatColor.GRAY + "Fix: Phase");
        }

        data.locations.addLocation(MiscUtils.getEntityBoundingBox(player), data);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if ((BlockUtils.isDoor(event.getClickedBlock())
                    || BlockUtils.isFenceGate(event.getClickedBlock())
                    || BlockUtils.isTrapDoor(event.getClickedBlock()))
                    && !event.isCancelled()) {
                PlayerData data = FunkePhase.getInstance().getDataManager().getPlayerData(event.getPlayer());

                if(data != null) {
                    data.lastDoorSwing = System.currentTimeMillis();
                }
            }
        }
    }

    public Location findSetback(PlayerData data) {
        List<BoundingBox> boxes = new ArrayList<>(data.locations.getBoundingBoxes());

        for(BoundingBox box : boxes) {
            if(box.getCollidingBlocks(data.player).size() == 0) {
                return box.getMinimum().toLocation(data.player.getWorld());
            }
        }
        return null;
    }
}
