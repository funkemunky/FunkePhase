package cc.funkemunky.funkephase.util;

import cc.funkemunky.funkephase.FunkePhase;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class MiscUtils {
    public static Map<EntityType, Vector> entityDimensions;

    public MiscUtils() {
        entityDimensions = new HashMap<>();

        entityDimensions.put(EntityType.WOLF, new Vector(0.31, 0.8, 0.31));
        entityDimensions.put(EntityType.SHEEP, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.COW, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.PIG, new Vector(0.45, 0.9, 0.45));
        entityDimensions.put(EntityType.MUSHROOM_COW, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.WITCH, new Vector(0.31, 1.95, 0.31));
        entityDimensions.put(EntityType.BLAZE, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.PLAYER, new Vector(0.3, 1.8, 0.3));
        entityDimensions.put(EntityType.VILLAGER, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.CREEPER, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.GIANT, new Vector(1.8, 10.8, 1.8));
        entityDimensions.put(EntityType.SKELETON, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.ZOMBIE, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.SNOWMAN, new Vector(0.35, 1.9, 0.35));
        entityDimensions.put(EntityType.HORSE, new Vector(0.7, 1.6, 0.7));
        entityDimensions.put(EntityType.ENDER_DRAGON, new Vector(1.5, 1.5, 1.5));
        entityDimensions.put(EntityType.ENDERMAN, new Vector(0.31, 2.9, 0.31));
        entityDimensions.put(EntityType.CHICKEN, new Vector(0.2, 0.7, 0.2));
        entityDimensions.put(EntityType.OCELOT, new Vector(0.31, 0.7, 0.31));
        entityDimensions.put(EntityType.SPIDER, new Vector(0.7, 0.9, 0.7));
        entityDimensions.put(EntityType.WITHER, new Vector(0.45, 3.5, 0.45));
        entityDimensions.put(EntityType.IRON_GOLEM, new Vector(0.7, 2.9, 0.7));
        entityDimensions.put(EntityType.GHAST, new Vector(2, 4, 2));
    }

    public static BoundingBox getEntityBoundingBox(LivingEntity entity) {
        if (entityDimensions.containsKey(entity.getType())) {
            Vector entityVector = entityDimensions.get(entity.getType());

            float minX = (float) Math.min(-entityVector.getX() + entity.getLocation().getX(), entityVector.getX() + entity.getLocation().getX());
            float minY = (float) Math.min(entity.getLocation().getY(), entityVector.getY() + entity.getLocation().getY());
            float minZ = (float) Math.min(-entityVector.getZ() + entity.getLocation().getZ(), entityVector.getZ() + entity.getLocation().getZ());
            float maxX = (float) Math.max(-entityVector.getX() + entity.getLocation().getX(), entityVector.getX() + entity.getLocation().getX());
            float maxY = (float) Math.max(entity.getLocation().getY(), entityVector.getY() + entity.getLocation().getY());
            float maxZ = (float) Math.max(-entityVector.getZ() + entity.getLocation().getZ(), entityVector.getZ() + entity.getLocation().getZ());
            return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        }
        return ReflectionsUtil.toBoundingBox(ReflectionsUtil.getBoundingBox(entity));
    }


    public static BoundingBox getPlayerBoxByLocation(Vector vector) {
        Vector min = vector.subtract(new Vector(0.3, 0, 0.3)), max = vector.add(new Vector(0.3, 1.8, 0.3));

        return new BoundingBox(min, max);
    }
    public static String line(String color) {
        return color + ChatColor.STRIKETHROUGH.toString() + "-----------------------------------------------------";
    }

}