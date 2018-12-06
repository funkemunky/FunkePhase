package cc.funkemunky.funkephase.util;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BoundingBox {

    public float minX, minY, minZ, maxX, maxY, maxZ;

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public BoundingBox(Vector min, Vector max) {
        this.minX = (float) Math.min(min.getX(), max.getX());
        this.minY = (float) Math.min(min.getY(), max.getY());
        this.minZ = (float) Math.min(min.getZ(), max.getZ());
        this.maxX = (float) Math.max(min.getX(), max.getX());
        this.maxY = (float) Math.max(min.getY(), max.getY());
        this.maxZ = (float) Math.max(min.getZ(), max.getZ());
    }

    public BoundingBox add(float x, float y, float z) {
        float newMinX = minX + x;
        float newMaxX = maxX + x;
        float newMinY = minY + y;
        float newMaxY = maxY + y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox add(Vector vector) {
        float x = (float) vector.getX(), y = (float) vector.getY(), z = (float) vector.getZ();

        float newMinX = minX + x;
        float newMaxX = maxX + x;
        float newMinY = minY + y;
        float newMaxY = maxY + y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox grow(float x, float y, float z) {
        float newMinX = minX - x;
        float newMaxX = maxX + x;
        float newMinY = minY - y;
        float newMaxY = maxY + y;
        float newMinZ = minZ - z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox shrink(float x, float y, float z) {
        float newMinX = minX + x;
        float newMaxX = maxX - x;
        float newMinY = minY + y;
        float newMaxY = maxY - y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ - z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox add(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new BoundingBox(this.minX + minX, this.minY + minY, this.minZ + minZ, this.maxX + maxX, this.maxY + maxY, this.maxZ + maxZ);
    }

    public BoundingBox subtract(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new BoundingBox(this.minX - minX, this.minY - minY, this.minZ - minZ, this.maxX - maxX, this.maxY - maxY, this.maxZ - maxZ);
    }

    public boolean intersectsWithBox(Vector vector) {
        return (vector.getX() > this.minX && vector.getX() < this.maxX) && ((vector.getY() > this.minY && vector.getY() < this.maxY) && (vector.getZ() > this.minZ && vector.getZ() < this.maxZ));
    }

    public List<BoundingBox> getCollidingBlockBoxes(Player player) {
        List<BoundingBox> toReturn = new ArrayList<>();
        int minX = MathUtils.floor(this.minX);
        int maxX = MathUtils.floor(this.maxX + 1);
        int minY = MathUtils.floor(this.minY);
        int maxY = MathUtils.floor(this.maxY + 1);
        int minZ = MathUtils.floor(this.minZ);
        int maxZ = MathUtils.floor(this.maxZ + 1);


        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = minY - 1; y < maxY; y++) {
                    Block block = new Location(player.getWorld(), x, y, z).getBlock();
                    if (BlockUtils.isSolid(block)) {
                        BoundingBox blockBox = ReflectionsUtil.getBlockBoundingBox(block);
                        if (BlockUtils.collisionBoundingBoxes.containsKey(block.getType())) {
                            for (BoundingBox box : BlockUtils.getBlockBoundingBox(block)) {
                                box.minX = box.minX == -69 ? blockBox.minX : box.minX;
                                box.minY = box.minY == -69 ? blockBox.minY : box.minY;
                                box.minZ = box.minZ == -69 ? blockBox.minZ : box.minZ;
                                box.maxX = box.maxX == -69 ? blockBox.maxX : box.maxX;
                                box.maxY = box.maxY == -69 ? blockBox.maxY : box.maxY;
                                box.maxZ = box.maxZ == -69 ? blockBox.maxZ : box.maxZ;

                                if (intersectsWithBox(box)) {
                                    toReturn.add(box);
                                    break;
                                }
                            }
                        } else {
                            if (intersectsWithBox(blockBox)) {
                                toReturn.add(blockBox);
                            }
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    public Vector getMinimum() {
        return new Vector(minX, minY, minZ);
    }

    public Vector getMaximum() {
        return new Vector(maxX, maxY, maxZ);
    }

    public List<Block> getCollidingBlocks(Player player) {
        List<Block> toReturn = new ArrayList<>();
        int minX = MathUtils.floor(this.minX);
        int maxX = MathUtils.floor(this.maxX + 1);
        int minY = MathUtils.floor(this.minY);
        int maxY = MathUtils.floor(this.maxY + 1);
        int minZ = MathUtils.floor(this.minZ);
        int maxZ = MathUtils.floor(this.maxZ + 1);


        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = minY - 1; y < maxY; y++) {
                    Location loc = new Location(player.getWorld(), x, y, z);

                    Block block = loc.getBlock();
                    if (BlockUtils.isSolid(block)) {
                        BoundingBox blockBox = ReflectionsUtil.getBlockBoundingBox(block);
                        if (BlockUtils.collisionBoundingBoxes.containsKey(block.getType())) {
                            for (BoundingBox box : BlockUtils.getBlockBoundingBox(block)) {
                                box.minX = box.minX == -69 ? blockBox.minX : box.minX;
                                box.minY = box.minY == -69 ? blockBox.minY : box.minY;
                                box.minZ = box.minZ == -69 ? blockBox.minZ : box.minZ;
                                box.maxX = box.maxX == -69 ? blockBox.maxX : box.maxX;
                                box.maxY = box.maxY == -69 ? blockBox.maxY : box.maxY;
                                box.maxZ = box.maxZ == -69 ? blockBox.maxZ : box.maxZ;

                                if (intersectsWithBox(box)) {
                                    toReturn.add(block);
                                    break;
                                }
                            }
                        } else {
                            if (intersectsWithBox(blockBox)) {
                                toReturn.add(block);
                            }
                        }
                    }
                }
            }
        }
        return toReturn;
    }


    public List<Block> getAllBlocks(Player player) {
        Location min = new Location(player.getWorld(), minX, minY, minZ).getBlock().getLocation();
        Location max = new Location(player.getWorld(), maxX, maxY, maxZ).getBlock().getLocation();
        List<Block> all = Lists.newArrayList();
        for (float x = (float) min.getX(); x < max.getX(); x++) {
            for (float y = (float) min.getY(); y < max.getY(); y++) {
                for (float z = (float) min.getZ(); z < max.getZ(); z++) {
                    Block block = new Location(player.getWorld(), x, y, z).getBlock();
                    if (!block.getType().equals(Material.AIR)) {
                        all.add(block);
                    }
                }
            }
        }
        return all;
    }

    public boolean inBlock(Player player) {
        return getCollidingBlocks(player).size() > 0;
    }

    public boolean intersectsWithBox(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY > this.minY && otherBox.minY < this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
        } else {
            float otherMinX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX > minX && otherMinX < maxX && otherMaxY > minY && otherMinY < maxY && otherMaxZ > minZ && otherMinZ < maxZ;
        }
    }

    public Object toAxisAlignedBB() {
        return ReflectionsUtil.newBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public String toString() {
        return "[" + minX + ", " + minY + ", " + minZ + ", " + maxX + ", " + maxY + ", " + maxZ + "]";
    }
}