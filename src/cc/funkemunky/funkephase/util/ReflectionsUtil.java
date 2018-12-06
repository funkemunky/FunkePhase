package cc.funkemunky.funkephase.util;

import cc.funkemunky.funkephase.FunkePhase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.logging.Level;

public class ReflectionsUtil {
    private static String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    public static Class<?> EntityPlayer = getNMSClass("EntityPlayer");
    private static Class<?> Entity = getNMSClass("Entity");
    private static Class<?> CraftPlayer = getCBClass("entity.CraftPlayer");
    private static Class<?> CraftEntity = getCBClass("entity.CraftEntity");
    private static Class<?> CraftWorld = getCBClass("CraftWorld");
    private static Class<?> World = getNMSClass("World");
    private static Class<?> worldServer = getNMSClass("WorldServer");
    private static Class<?> iBlockData;
    private static Class<?> blockPosition;
    private static Class<?> vanillaBlock = getNMSClass("Block");
    private static Method getCubes = getMethod(World, "a", getNMSClass("AxisAlignedBB"));
    private static Method getCubes1_12 = getMethod(World, "getCubes", getNMSClass("Entity"), getNMSClass("AxisAlignedBB"));

    public ReflectionsUtil() {
        if (!isBukkitVerison("1_7")) {
            iBlockData = getNMSClass("IBlockData");
            blockPosition = getNMSClass("BlockPosition");
        }
    }

    public static Object getEntityPlayer(Player player) {
        return getMethodValue(getMethod(CraftPlayer, "getHandle"), player);
    }

    public static Object getEntity(Entity entity) {
        return getMethodValue(getMethod(CraftEntity, "getHandle"), entity);
    }

    public static Object getExpandedBoundingBox(Object box, double x, double y, double z) {
        return getMethodValue(getMethod(box.getClass(), "grow", double.class, double.class, double.class), box, x, y, z);
    }

    public static Object modifyBoundingBox(Object box, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double newminX = (double) getFieldValue(getFieldByName(box.getClass(), "a"), box) - minX;
        double newminY = (double) getFieldValue(getFieldByName(box.getClass(), "b"), box) - minY;
        double newminZ = (double) getFieldValue(getFieldByName(box.getClass(), "c"), box) - minZ;
        double newmaxX = (double) getFieldValue(getFieldByName(box.getClass(), "d"), box) + maxX;
        double newmaxY = (double) getFieldValue(getFieldByName(box.getClass(), "e"), box) + maxY;
        double newmaxZ = (double) getFieldValue(getFieldByName(box.getClass(), "f"), box) + maxZ;

        return newInstance(getNMSClass("AxisAlignedBB"), newminX, newminY, newminZ, newmaxX, newmaxY, newmaxZ);
    }

    private static Vector getBoxMin(Object box) {
        double x = (double) getFieldValue(getFieldByName(box.getClass(), "a"), box);
        double y = (double) getFieldValue(getFieldByName(box.getClass(), "b"), box);
        double z = (double) getFieldValue(getFieldByName(box.getClass(), "c"), box);
        return new Vector(x, y, z);
    }

    private static Vector getBoxMax(Object box) {
        double x = (double) getFieldValue(getFieldByName(box.getClass(), "d"), box);
        double y = (double) getFieldValue(getFieldByName(box.getClass(), "e"), box);
        double z = (double) getFieldValue(getFieldByName(box.getClass(), "f"), box);
        return new Vector(x, y, z);
    }

    public static BoundingBox toBoundingBox(Object aaBB) {
        Vector min = getBoxMin(aaBB);
        Vector max = getBoxMax(aaBB);

        return new BoundingBox((float) min.getX(), (float) min.getY(), (float) min.getZ(), (float) max.getX(), (float) max.getY(), (float) max.getZ());
    }

    public static float getBlockDurability(Block block) {
        Object vanillaBlock = getVanillaBlock(block);
        return (float) getFieldValue(getFieldByName(getNMSClass("Block"), "durability"), vanillaBlock);
    }

    public static float getFriction(Block block) {
        //net.minecraft.server.v1_8_R3.EntityPlayer eplayer = (net.minecraft.server.v1_8_R3.EntityPlayer) getEntityPlayer(player);

        Object blockNMS = getVanillaBlock(block);


        return (float) getFieldValue(getFieldByName(vanillaBlock, "frictionFactor"), blockNMS);
        //eplayer.world.getType(new BlockPosition(eplayer.locX, eplayer.locY, eplayer.locZ)).getBlock().frictionFactor;
    }

    public static BoundingBox getBlockBoundingBox(Block block) {
        try {
            if (!isBukkitVerison("1_7")) {
                Object bPos = blockPosition.getConstructor(int.class, int.class, int.class).newInstance(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                Object world = getWorldHandle(block.getWorld());
                Object data = getMethodValue(getMethod(world.getClass(), "getType", blockPosition), world, bPos);
                Object blockNMS = getMethodValue(getMethod(getNMSClass("IBlockData"), "getBlock"), data);

                if (!isNewVersion()) {

                    if (getMethodValueNoST(getMethodNoST(blockNMS.getClass(), "a", World, blockPosition, iBlockData), blockNMS, world, bPos, data) != null
                            && !BlockUtils.isSlab(block)) {
                        BoundingBox box = toBoundingBox(getMethodValue(getMethod(blockNMS.getClass(), "a", World, blockPosition, iBlockData), blockNMS, world, bPos, data));

                        if (block.getType().equals(Material.STEP)) {
                            Step slab = (Step) block.getType().getNewData(block.getData());

                            box.minY = block.getY();
                            box.maxY = block.getY();
                            if (slab.isInverted()) {
                                box = box.add(0, 0.5f, 0, 0, 1f, 0);
                            } else {
                                box = box.add(0, 0f, 0, 0, 0.5f, 0);
                            }
                        } else if (block.getType().equals(Material.WOOD_STEP)) {
                            WoodenStep slab = (WoodenStep) block.getType().getNewData(block.getData());

                            box.minY = block.getY();
                            box.maxY = block.getY();
                            if (slab.isInverted()) {
                                box = box.add(0, 0.5f, 0, 0, 1f, 0);
                            } else {
                                box = box.add(0, 0f, 0, 0, 0.5f, 0);
                            }
                        }
                        return box;
                    } else if (getMethodValueNoST(getMethodNoST(vanillaBlock, "a", World, blockPosition, iBlockData), blockNMS, world, bPos, data) != null) {
                        BoundingBox box = toBoundingBox(getMethodValue(getMethod(vanillaBlock, "a", World, blockPosition, iBlockData), blockNMS, world, bPos, data));

                        if (block.getType().equals(Material.STEP)) {
                            Step slab = (Step) block.getType().getNewData(block.getData());

                            box.minY = block.getY();
                            box.maxY = block.getY();
                            if (slab.isInverted()) {
                                box = box.add(0, 0.5f, 0, 0, 1f, 0);
                            } else {
                                box = box.add(0, 0f, 0, 0, 0.5f, 0);
                            }
                        } else if (block.getType().equals(Material.WOOD_STEP)) {
                            WoodenStep slab = (WoodenStep) block.getType().getNewData(block.getData());

                            box.minY = block.getY();
                            box.maxY = block.getY();
                            if (slab.isInverted()) {
                                box = box.add(0, 0.5f, 0, 0, 1f, 0);
                            } else {
                                box = box.add(0, 0f, 0, 0, 0.5f, 0);
                            }
                        }
                        return box;
                    } else {
                        return new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX(), block.getY(), block.getZ());
                    }
                } else {
                    if (getMethodValueNoST(getMethodNoST(blockNMS.getClass(), "a", iBlockData, getNMSClass("IBlockAccess"), blockPosition), blockNMS, data, world, bPos) != null) {
                        return toBoundingBox(getMethodValue(getMethod(blockNMS.getClass(), "a", iBlockData, getNMSClass("IBlockAccess"), blockPosition), blockNMS, data, world, bPos)).add(block.getX(), block.getY(), block.getZ(), block.getX(), block.getY(), block.getZ());
                    } else if (getMethodValueNoST(getMethodNoST(vanillaBlock, "a", iBlockData, getNMSClass("IBlockAccess"), blockPosition), blockNMS, data, world, bPos) != null) {
                        return toBoundingBox(getMethodValue(getMethod(vanillaBlock, "a", iBlockData, getNMSClass("IBlockAccess"), blockPosition), blockNMS, data, world, bPos)).add(block.getX(), block.getY(), block.getZ(), block.getX(), block.getY(), block.getZ());
                    } else {
                        return new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX(), block.getY(), block.getZ());
                    }
                }
            } else {
                Object blockNMS = getVanillaBlock(block);
                Object world = getWorldHandle(block.getWorld());
                if (getMethodValueNoST(getMethodNoST(vanillaBlock, "a", getNMSClass("World"), int.class, int.class, int.class), blockNMS, world, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()) != null) {
                    return toBoundingBox(getMethodValue(getMethod(vanillaBlock, "a", getNMSClass("World"), int.class, int.class, int.class), blockNMS, world, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()));
                } else {
                    //Bukkit.broadcastMessage(block.getType().name());
                    return new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX(), block.getY(), block.getZ());
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error occured with block: " + block.getType().toString());
            e.printStackTrace();
        }
        return null;
    }


    public static Object getVanillaBlock(Block block) {

        if (!isBukkitVerison("1_7")) {
            Object getType = getBlockData(block);
            return getMethodValue(getMethod(iBlockData, "getBlock"), getType);
        } else {
            Object world = getWorldHandle(block.getWorld());
            return getMethodValue(getMethod(worldServer, "getType", int.class, int.class, int.class), world, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
        }
    }

    private static Object getBlockData(Block block) {
        Location loc = block.getLocation();
        try {
            if (!isBukkitVerison("1_7")) {
                Object bPos = blockPosition.getConstructor(int.class, int.class, int.class).newInstance(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                Object world = getWorldHandle(block.getWorld());
                return getMethodValue(getMethod(worldServer, "getType", blockPosition), world, bPos);
            } else {
                Object world = getWorldHandle(block.getWorld());
                return getMethodValue(getMethod(worldServer, "getType", int.class, int.class, int.class), world, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getBelowBox(Player player, double below) {
        Object box = getBoundingBox(player);
        double minX = (double) getFieldValue(getFieldByName(box.getClass(), "a"), box);
        double minY = (double) getFieldValue(getFieldByName(box.getClass(), "b"), box) - below;
        double minZ = (double) getFieldValue(getFieldByName(box.getClass(), "c"), box);
        double maxX = (double) getFieldValue(getFieldByName(box.getClass(), "d"), box);
        double maxY = (double) getFieldValue(getFieldByName(box.getClass(), "e"), box);
        double maxZ = (double) getFieldValue(getFieldByName(box.getClass(), "f"), box);

        try {
            return getNMSClass("AxisAlignedBB").getConstructor(double.class, double.class, double.class, double.class, double.class, double.class).newInstance(minX, minY, minZ, maxX, maxY, maxZ);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getBoundingBox(Player player) {
        return getBoundingBox((Entity) player);
    }

    public static Object getBoundingBox(Entity entity) {
        return isBukkitVerison("1_7") ? getFieldValue(getFieldByName(Entity, "boundingBox"), getEntity(entity)) : getMethodValue(getMethod(Entity, "getBoundingBox"), getEntity(entity));
    }

    public static boolean isBukkitVerison(String version) {
        return FunkePhase.getInstance().getServerVersion().contains(version);
    }

    public static boolean isNewVersion() {
        return isBukkitVerison("1_9") || isBukkitVerison("1_1");
    }

    public static Class<?> getCBClass(String string) {
        return getClass("org.bukkit.craftbukkit." + version + "." + string);
    }


    public static Object newBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        try {
            return isBukkitVerison("1_7") ? getMethodValue(getMethod(getNMSClass("AxisAlignedBB"), "a", double.class, double.class, double.class, double.class, double.class, double.class), null, minX, minY, minZ, maxX, maxY, maxZ) : getNMSClass("AxisAlignedBB").getConstructor(double.class, double.class, double.class, double.class, double.class, double.class).newInstance(minX, minY, minZ, maxX, maxY, maxZ);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object newBoundingBox(Location from, Location to) {
        double minX = Math.min(from.getX(), to.getX());
        double minY = Math.min(from.getY(), to.getY());
        double minZ = Math.min(from.getZ(), to.getZ());
        double maxX = Math.max(from.getX(), to.getX());
        double maxY = Math.max(from.getY(), to.getY());
        double maxZ = Math.max(from.getZ(), to.getZ());

        try {
            return getNMSClass("AxisAlignedBB").getConstructor(double.class, double.class, double.class, double.class, double.class, double.class).newInstance(minX, minY, minZ, maxX, maxY, maxZ);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getClass(String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Enum<?> getEnum(Class<?> clazz, String enumName) {
        return Enum.valueOf((Class<Enum>) clazz, enumName);
    }

    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName) != null ? clazz.getDeclaredField(fieldName) : clazz.getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);

            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object setFieldValue(Object object, Field field, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return field.getDeclaringClass();
    }


    public static boolean inBlock(Player player, Object axisAlignedBB) {
        return getCollidingBlocks(player, axisAlignedBB).size() > 0;
    }

    /**
     * Method removed in 1.12 and later versions in NMS
     **/
    public static Collection<?> getCollidingBlocks(Player player, Object axisAlignedBB) {
        Object world = getWorldHandle(player.getWorld());
        return (Collection<?>) (isNewVersion()
                ? getMethodValue(getCubes1_12, world, null, axisAlignedBB)
                : getMethodValue(getCubes, world, axisAlignedBB));
    }

    public static Object getWorldHandle(org.bukkit.World world) {
        return getMethodValue(getMethod(CraftWorld, "getHandle"), world);
    }

    public static Field getFirstFieldByType(Class<?> clazz, Class<?> type) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().equals(type)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... args) {
        try {
            Method method = clazz.getMethod(methodName, args);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Method getMethodNoST(Class<?> clazz, String methodName, Class<?>... args) {
        try {
            Method method = clazz.getMethod(methodName, args);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
        }
        return null;
    }

    public static Object getMethodValue(Method method, Object object, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getMethodValueNoST(Method method, Object object, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValueNoST(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static Field getFieldByNameNoST(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName) != null ? clazz.getDeclaredField(fieldName) : clazz.getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);

            return field;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object newInstance(Class<?> objectClass, Object... args) {
        try {
            return objectClass.getConstructor(args.getClass()).newInstance(args);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getNMSClass(String string) {
        return getClass("net.minecraft.server." + version + "." + string);
    }
}