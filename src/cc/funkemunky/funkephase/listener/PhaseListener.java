package cc.funkemunky.funkephase.listener;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.utils.*;
import cc.funkemunky.funkephase.FunkePhase;
import cc.funkemunky.funkephase.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class PhaseListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        PlayerData data = FunkePhase.getInstance().getDataManager().getPlayerData(event.getPlayer());

        if(data != null) {
            data.setLastTeleport(System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPhase(PlayerMoveEvent e) {
        Atlas.getInstance().getThreadPool().execute(() -> {
            Player player = e.getPlayer();
            PlayerData data = FunkePhase.getInstance().getDataManager().getPlayerData(player);

<<<<<<< HEAD
            if (player.getAllowFlight()
                    || player.getVehicle() != null
                    || !FunkePhase.getInstance().toggled
                    || MathUtils.elapsed(data.lastDoorSwing) < 500) {
                return;
            }
=======
        if(data == null) return;

        long timestamp = System.currentTimeMillis();

        if (player.getAllowFlight()
                || e.getTo().getWorld().getUID() != e.getFrom().getWorld().getUID()
                || player.getVehicle() != null
                || !FunkePhase.getInstance().toggled
                || timestamp - data.lastTeleport < 70L
                || timestamp - data.lastDoorSwing < 500) {
            return;
        }
>>>>>>> ed2876be8253bf03301c394cf8c8fd3a08b40c0c

            if (e.getFrom().distance(e.getTo()) > 10) {
                e.setCancelled(true);
                return;
            }

<<<<<<< HEAD
            float minX = (float) Math.min(e.getFrom().getX(), e.getTo().getX()), minY = (float) Math.min(e.getFrom().getY(), e.getTo().getY()), minZ = (float) Math.min(e.getFrom().getZ(), e.getTo().getZ()),
                    maxX = (float) Math.max(e.getFrom().getX(), e.getTo().getX()), maxY = (float) Math.max(e.getFrom().getY(), e.getTo().getY()), maxZ = (float) Math.max(e.getFrom().getZ(), e.getTo().getZ());
=======
        if(data.isEnderPearlTeleport()) {
            data.setEnderPearlTeleport(false);
            return;
        }

        float minX = (float) Math.min(e.getFrom().getX(), e.getTo().getX()), minY = (float) Math.min(e.getFrom().getY(), e.getTo().getY()), minZ = (float) Math.min(e.getFrom().getZ(), e.getTo().getZ()),
                maxX = (float) Math.max(e.getFrom().getX(), e.getTo().getX()), maxY = (float) Math.max(e.getFrom().getY(), e.getTo().getY()), maxZ = (float) Math.max(e.getFrom().getZ(), e.getTo().getZ());
>>>>>>> ed2876be8253bf03301c394cf8c8fd3a08b40c0c

            BoundingBox box = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ).add(0f, 0f, 0f, 0f, 1.8f, 0f).shrink(0.1f,0.1f,0.1f);

            if (Atlas.getInstance().getBlockBoxManager().getBlockBox().getCollidingBoxes(player.getWorld(), box).stream().anyMatch(aabb -> {
                Block block = BlockUtils.getBlock(aabb.getMinimum().toLocation(data.player.getWorld()));
                return box.intersectsWithBox(aabb) && BlockUtils.isSolid(block) && !FunkePhase.getInstance().getExcludedBlocks().contains(block.getType());
            })) {
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
        });
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
