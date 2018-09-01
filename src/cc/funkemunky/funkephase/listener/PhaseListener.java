/*
 * FunkePhase
 * Copyright (C) 2018 funkemunky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.funkemunky.funkephase.listener;

import cc.funkemunky.funkephase.FunkePhase;
import cc.funkemunky.funkephase.util.BlockUtils;
import cc.funkemunky.funkephase.util.BoundingBox;
import cc.funkemunky.funkephase.util.MathUtils;
import cc.funkemunky.funkephase.util.ReflectionsUtil;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Collections;
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
        if(FunkePhase.instance.toggled) {
            Player player = e.getPlayer();

            if(player.getAllowFlight()
                    || player.getVehicle() != null
                    || MathUtils.elapsed(lastDoorSwing.getOrDefault(player, 0L)) < 500) {
                return;
            }

            if(e.getFrom().distance(e.getTo()) > FunkePhase.instance.maxMove) {
                e.setTo(e.getFrom());
                return;
            }

            float minX = (float) Math.min(e.getFrom().getX(), e.getTo().getX()), minY = (float) Math.min(e.getFrom().getY(), e.getTo().getY()), minZ = (float) Math.min(e.getFrom().getZ(), e.getTo().getZ()),
                    maxX = (float) Math.max(e.getFrom().getX(), e.getTo().getX()), maxY = (float) Math.max(e.getFrom().getY(), e.getTo().getY()), maxZ = (float) Math.max(e.getFrom().getZ(), e.getTo().getZ());

            BoundingBox box = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ).add(0,0,0,0,1.8f,0);

            final List<String> blocks = new ArrayList<>();
            List<Block> colliding = box.getCollidingBlocks(e.getPlayer());
            colliding.stream().forEach(block -> blocks.add(block.getType().name().toLowerCase()));
            if (colliding.stream().anyMatch(block -> !FunkePhase.instance.excludedBlocks.contains(block.getType()))) {
                e.setTo(e.getFrom());

                FunkePhase.instance.alert(e.getPlayer(), blocks);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK
                && FunkePhase.instance.toggled) {
            if((BlockUtils.isDoor(event.getClickedBlock())
                    || BlockUtils.isFenceGate(event.getClickedBlock())
                    || BlockUtils.isTrapDoor(event.getClickedBlock()))
                    && !event.isCancelled()) {
                lastDoorSwing.put(event.getPlayer(), System.currentTimeMillis());
            }
        }
    }
}
