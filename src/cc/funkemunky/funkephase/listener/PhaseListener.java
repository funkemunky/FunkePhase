package cc.funkemunky.funkephase.listener;

import cc.funkemunky.funkephase.FunkePhase;
import cc.funkemunky.funkephase.util.BlockUtils;
import cc.funkemunky.funkephase.util.BoundingBox;
import cc.funkemunky.funkephase.util.MathUtils;
import cc.funkemunky.funkephase.util.ReflectionsUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class PhaseListener implements Listener {

    private Map<Player, Long> lastDoorSwing;

    public PhaseListener() {
        lastDoorSwing = new WeakHashMap<>();

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPhase(PlayerMoveEvent e) {
        if (FunkePhase.instance.toggled) {
            Player player = e.getPlayer();

            if (player.getAllowFlight()
                    || player.getVehicle() != null
                    || MathUtils.elapsed(lastDoorSwing.getOrDefault(player, 0L)) < 500) {
                return;
            }

            if (e.getFrom().distance(e.getTo()) > FunkePhase.instance.maxMove) {
                e.setTo(e.getFrom());
                return;
            }

            float minX = (float) Math.min(e.getFrom().getX(), e.getTo().getX()), minY = (float) Math.min(e.getFrom().getY(), e.getTo().getY()), minZ = (float) Math.min(e.getFrom().getZ(), e.getTo().getZ()),
                    maxX = (float) Math.max(e.getFrom().getX(), e.getTo().getX()), maxY = (float) Math.max(e.getFrom().getY(), e.getTo().getY()), maxZ = (float) Math.max(e.getFrom().getZ(), e.getTo().getZ());

            Object box = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ).add(0f, 0f, 0f, 0f, 1.8f, 0f).toAxisAlignedBB();

            if(ReflectionsUtil.getCollidingBlocks(e.getPlayer(), box).size() > 0) {
                e.setTo(e.getFrom());
                FunkePhase.instance.alert(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && FunkePhase.instance.toggled) {
            if ((BlockUtils.isDoor(event.getClickedBlock())
                    || BlockUtils.isFenceGate(event.getClickedBlock())
                    || BlockUtils.isTrapDoor(event.getClickedBlock()))
                    && !event.isCancelled()) {
                lastDoorSwing.put(event.getPlayer(), System.currentTimeMillis());
            }
        }
    }
}
