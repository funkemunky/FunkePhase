package cc.funkemunky.funkephase.listener;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.utils.*;
import cc.funkemunky.api.utils.world.BlockData;
import cc.funkemunky.api.utils.world.CollisionBox;
import cc.funkemunky.api.utils.world.EntityData;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import cc.funkemunky.funkephase.FunkePhase;
import cc.funkemunky.funkephase.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PhaseListener implements Listener {

    private static final Material AIR = XMaterial.AIR.parseMaterial();

    @EventHandler(priority = EventPriority.LOW)
    public void onPhase(PlayerMoveEvent e) {
        FunkePhase.INSTANCE.getService().execute(() -> {
            Player player = e.getPlayer();
            PlayerData data = FunkePhase.INSTANCE.getDataManager().getPlayerData(player);

            long timestamp = System.currentTimeMillis();

            if (data == null
                    || player.getAllowFlight()
                    || e.getTo().getWorld().getUID() != e.getFrom().getWorld().getUID()
                    || player.getVehicle() != null
                    || !FunkePhase.INSTANCE.phaseEnabled
                    || timestamp - data.lastDoorSwing < 500) {
                return;
            }

            if (e.getFrom().distanceSquared(e.getTo())
                    > (FunkePhase.INSTANCE.getMaxMove() * FunkePhase.INSTANCE.getMaxMove())) {
                e.setCancelled(true);
                return;
            }

            final float minX = (float) Math.min(e.getFrom().getX(), e.getTo().getX()),
                    minY = (float) Math.min(e.getFrom().getY(), e.getTo().getY()),
                    minZ = (float) Math.min(e.getFrom().getZ(), e.getTo().getZ()),
                    maxX = (float) Math.max(e.getFrom().getX(), e.getTo().getX()),
                    maxY = (float) Math.max(e.getFrom().getY(), e.getTo().getY()),
                    maxZ = (float) Math.max(e.getFrom().getZ(), e.getTo().getZ());

            final SimpleCollisionBox box = new SimpleCollisionBox(minX, minY, minZ, maxX, maxY + 1.8f, maxZ)
                    .shrink(0.1f, 0.1f, 0.1f);

            int x1 = (int) Math.floor(box.xMin);
            int y1 = (int) Math.floor(box.yMin);
            int z1 = (int) Math.floor(box.zMin);
            int x2 = (int) Math.ceil(box.xMax);
            int y2 = (int) Math.ceil(box.yMax);
            int z2 = (int) Math.ceil(box.zMax);

            for (int x = x1; x <= x2; ++x) {
                for (int y = y1; y <= y2; ++y) {
                    for (int z = z1; z <= z2; ++z) {
                        Block block;
                        Material material;
                        if ((block = Helper.getBlockAt(e.getTo().getWorld(), x, y, z)) != null
                                && (material = block.getType()) != AIR
                                && Materials.checkFlag(material, Materials.SOLID)
                                && !FunkePhase.INSTANCE.getExcludedBlocks().contains(material)) {
                            CollisionBox blockBox = BlockData.getData(material)
                                    .getBox(block, ProtocolVersion.getGameVersion());

                            if (blockBox.isIntersected(box)) {
                                Location setback = findSetback(data);

                                if (setback != null) {
                                    setback.setPitch(e.getTo().getPitch());
                                    setback.setYaw(e.getTo().getYaw());
                                }
                                e.setTo(setback != null ? setback : e.getFrom());
                                FunkePhase.INSTANCE.alert(e.getPlayer());
                                return;
                            }
                        }
                    }
                }
            }

            data.locations.addLocation((SimpleCollisionBox) EntityData.getEntityBox(e.getTo(), player));
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if ((BlockUtils.isDoor(event.getClickedBlock())
                    || BlockUtils.isFenceGate(event.getClickedBlock())
                    || BlockUtils.isTrapDoor(event.getClickedBlock()))
                    && !event.isCancelled()) {
                PlayerData data = FunkePhase.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

                if (data != null) {
                    data.lastDoorSwing = System.currentTimeMillis();
                }
            }
        }
    }

    public Location findSetback(PlayerData data) {
        synchronized (data.locations.boundingBoxes) {
            for (SimpleCollisionBox box : data.locations.boundingBoxes) {
                if (Helper.getBlocksNearby2(data.getPlayer().getWorld(), box, Materials.SOLID).stream()
                        .noneMatch(block -> BlockData.getData(block.getType())
                                .getBox(block, ProtocolVersion.getGameVersion()).isCollided(box))) {
                    return box.min().toLocation(data.player.getWorld());
                }
            }
            return null;
        }
    }
}
