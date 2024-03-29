package cc.funkemunky.funkephase.listener;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.utils.*;
import cc.funkemunky.api.utils.world.BlockData;
import cc.funkemunky.api.utils.world.CollisionBox;
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        PlayerData data = FunkePhase.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if(data == null) return; //Making sure PlayerData is not null before we proceed.

        data.setLastTeleport(event.getTo().clone());
        data.lastTeleportTime = System.currentTimeMillis();

        //Fixing potential exploit/bug where players will phase and get teleported back
        data.locations.locations.clear();
        data.locations.addLocation(event.getTo().clone());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPhase(PlayerMoveEvent event) {
        PlayerData data = FunkePhase.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if(data == null) return; //Making sure PlayerData is not null before we proceed.

        long timestamp = System.currentTimeMillis();

        //This means the player has teleported
        if(data.getLastTeleport() != null && event.getTo().equals(data.getLastTeleport())) {
            //Just making sure duplicate location attempts do not return for bypass
            if(timestamp - data.lastTeleportTime > 2000L) data.setLastTeleport(null);
            else {
                return;
            }
        }

        Player player = event.getPlayer();

        if (player.getAllowFlight()
                || event.getTo().getWorld().getUID() != event.getFrom().getWorld().getUID()
                || player.getVehicle() != null
                || !FunkePhase.INSTANCE.phaseEnabled
                || timestamp - data.lastDoorSwing < 500) {
            return;
        }

        if (event.getFrom().distanceSquared(event.getTo())
                > (FunkePhase.INSTANCE.getMaxMove() * FunkePhase.INSTANCE.getMaxMove())) {
            event.setCancelled(true);
            return;
        }

        final float minX = (float) Math.min(event.getFrom().getX(), event.getTo().getX()),
                minY = (float) Math.min(event.getFrom().getY(), event.getTo().getY()),
                minZ = (float) Math.min(event.getFrom().getZ(), event.getTo().getZ()),
                maxX = (float) Math.max(event.getFrom().getX(), event.getTo().getX()),
                maxY = (float) Math.max(event.getFrom().getY(), event.getTo().getY()),
                maxZ = (float) Math.max(event.getFrom().getZ(), event.getTo().getZ());

        final SimpleCollisionBox box = new SimpleCollisionBox(minX, minY, minZ, maxX, maxY + 1.8f, maxZ)
                .shrink(0.05f, 0.05f, 0.05f);

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
                    if ((block = Helper.getBlockAt(event.getTo().getWorld(), x, y, z)) != null
                            && (material = block.getType()) != AIR
                            && Materials.checkFlag(material, Materials.SOLID)
                            && !FunkePhase.INSTANCE.getExcludedBlocks().contains(material)) {
                        CollisionBox blockBox = BlockData.getData(material)
                                .getBox(block, ProtocolVersion.getGameVersion());

                        if (blockBox.isIntersected(box)) {
                            Location setback = findSetback(data);

                            if (setback != null) {
                                setback.setPitch(event.getTo().getPitch());
                                setback.setYaw(event.getTo().getYaw());
                            }
                            event.getPlayer().teleport(setback != null ? setback : event.getFrom());
                            FunkePhase.INSTANCE.alert(event.getPlayer());
                            return;
                        }
                    }
                }
            }
        }

        data.locations.addLocation(event.getFrom().clone());
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
        synchronized (data.locations.locations) {
            return data.locations.locations.getFirst();
        }
    }
}
