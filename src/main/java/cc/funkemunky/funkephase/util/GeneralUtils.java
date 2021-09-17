package cc.funkemunky.funkephase.util;

import cc.funkemunky.api.utils.BoundingBox;
import cc.funkemunky.api.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class GeneralUtils {

    public static BoundingBox getPlayerBoxByLocation(Vector vector) {
        Vector min = vector.subtract(new Vector(0.3, 0, 0.3)), max = vector.add(new Vector(0.3, 1.8, 0.3));

        return new BoundingBox(min, max);
    }
    public static String line(String color) {
        return color + ChatColor.STRIKETHROUGH.toString() + "-----------------------------------------------------";
    }

    public static boolean isInSolidBlock(BoundingBox box, World world) {
        int minX = MathUtils.floor(box.minX);
        int maxX = MathUtils.floor(box.maxX + 1);
        int minY = MathUtils.floor(box.minY);
        int maxY = MathUtils.floor(box.maxY + 1);
        int minZ = MathUtils.floor(box.minZ);
        int maxZ = MathUtils.floor(box.maxZ + 1);
        for(double x = minX ; x < maxX ; x++) {
            for(double y = minY ; y < maxY ; y++) {
                for(double z = minZ ; z < maxZ ; z++) {
                    if(new Location(world, x, y, z).getBlock().getType().isSolid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}