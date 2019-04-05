package cc.funkemunky.funkephase.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.*;

public class BlockUtils {

    static Map<Material, BoundingBox[]> collisionBoundingBoxes;

    public BlockUtils() {
        collisionBoundingBoxes = new HashMap<>();

        setupCollisionBB();
    }

    public static boolean isSolid(Block block) {
        int type = block.getType().getId();

        switch (type) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 29:
            case 34:
            case 33:
            case 35:
            case 36:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 60:
            case 61:
            case 62:
            case 64:
            case 65:
            case 67:
            case 71:
            case 73:
            case 74:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 116:
            case 117:
            case 118:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 144:
            case 145:
            case 146:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            case 198:
            case 199:
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
            case 210:
            case 211:
            case 212:
            case 213:
            case 214:
            case 215:
            case 216:
            case 218:
            case 219:
            case 220:
            case 221:
            case 222:
            case 223:
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 229:
            case 230:
            case 231:
            case 232:
            case 233:
            case 234:
            case 235:
            case 236:
            case 237:
            case 238:
            case 239:
            case 240:
            case 241:
            case 242:
            case 243:
            case 244:
            case 245:
            case 246:
            case 247:
            case 248:
            case 249:
            case 250:
            case 251:
            case 252:
            case 255:
            case 397:
            case 355:
                return true;

        }
        return false;
    }

    public static BoundingBox[] getBlockBoundingBox(Block block) {
        if (collisionBoundingBoxes.containsKey(block.getType())) {
            BoundingBox[] newBox = collisionBoundingBoxes.get(block.getType());

            return new BoundingBox[]{newBox[0].add((float) (newBox[0].minX != -69 ? block.getLocation().getX() : 0), (float) (newBox[0].minY != -69 ? block.getLocation().getY() : 0), (float) (newBox[0].minZ != -69 ? block.getLocation().getZ() : 0), (float) (newBox[0].maxX != -69 ? block.getLocation().getX() : 0), (float) (newBox[0].maxY != -69 ? block.getLocation().getY() : 0), (float) (newBox[0].maxZ != -69 ? block.getLocation().getZ() : 0)), newBox[1].add((float) (newBox[1].minX != -69 ? block.getLocation().getX() : 0), (float) (newBox[1].minY != -69 ? block.getLocation().getY() : 0), (float) (newBox[1].minZ != -69 ? block.getLocation().getZ() : 0), (float) (newBox[1].maxX != -69 ? block.getLocation().getX() : 0), (float) (newBox[1].maxY != -69 ? block.getLocation().getY() : 0), (float) (newBox[1].maxZ != -69 ? block.getLocation().getZ() : 0))};
        }
        BoundingBox box = ReflectionsUtil.getBlockBoundingBox(block);

        if (box != null) {
            return new BoundingBox[]{box, new BoundingBox(0, 0, 0, 0, 0, 0)};
        }
        return new BoundingBox[]{new BoundingBox(0, 0, 0, 0, 0, 0), new BoundingBox(0, 0, 0, 0, 0, 0)};
    }

    public static boolean isDoor(Block block) {
        return block.getType().equals(Material.IRON_DOOR) || block.getType().equals(Material.getMaterial("IRON_DOOR_BLOCK")) || block.getType().equals(Material.getMaterial("WOOD_DOOR")) || block.getType().equals(Material.getMaterial("WOODEN_DOOR")) || block.getType().getId() == 193 || block.getType().getId() == 194 || block.getType().getId() == 195 || block.getType().getId() == 196 || block.getType().getId() == 197 || block.getType().getId() == 324 || block.getType().getId() == 428 || block.getType().getId() == 429 || block.getType().getId() == 430 || block.getType().getId() == 431;
    }

    public static boolean isTrapDoor(Block block) {
        return block.getType().getId() == 96 || block.getType().getId() == 167;
    }
    public static boolean isFenceGate(Block block) {
        return block.getType().getId() == 107 || block.getType().getId() == 183 || block.getType().getId() == 184 || block.getType().getId() == 185 || block.getType().getId() == 186 || block.getType().getId() == 187;
    }

    public static boolean isSlab(Block block) {
        return block.getType().getId() == 44 || block.getType().getId() == 126 || block.getType().getId() == 205 || block.getType().getId() == 182;
    }

    private void setupCollisionBB() {
        collisionBoundingBoxes.put(Material.FIRE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DAYLIGHT_DETECTOR, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.375, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.STONE_PLATE, new BoundingBox[]{new BoundingBox((float) 0.0625, (float) 0.0, (float) 0.0625, (float) 0.9375, (float) 0.0625, (float) 0.9375), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.GRAVEL, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.COBBLESTONE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.ENDER_CHEST, new BoundingBox[]{new BoundingBox((float) 0.0625, (float) 0.0, (float) 0.0625, (float) 0.9375, (float) 0.875, (float) 0.9375), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.NETHER_BRICK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.PUMPKIN, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.CARROT, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.25, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.TNT, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SAND, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.WOOD_PLATE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SIGN_POST, new BoundingBox[]{new BoundingBox((float) 0.25, (float) 0.0, (float) 0.25, (float) 0.75, (float) 1.0, (float) 0.75), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.COCOA, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DETECTOR_RAIL, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.125, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.HARD_CLAY, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.NETHERRACK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.STONE_BUTTON, new BoundingBox[]{new BoundingBox((float) 0.3125, (float) 0.0, (float) 0.375, (float) 0.6875, (float) 0.125, (float) 0.625), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.STAINED_GLASS, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.STAINED_GLASS_PANE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.CLAY, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.ACTIVATOR_RAIL, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.125, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.QUARTZ_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.HUGE_MUSHROOM_1, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.HUGE_MUSHROOM_2, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.BEACON, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.GRASS, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DEAD_BUSH, new BoundingBox[]{new BoundingBox((float) 0.09999999403953552, (float) 0.0, (float) 0.09999999403953552, (float) 0.8999999761581421, (float) 0.800000011920929, (float) 0.8999999761581421), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.GLOWSTONE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.ICE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.BRICK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.REDSTONE_TORCH_ON, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.REDSTONE_TORCH_OFF, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.POWERED_RAIL, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.125, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DISPENSER, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.ANVIL, new BoundingBox[]{new BoundingBox((float) 0.125, (float) 0.0, (float) 0.0, (float) 0.875, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.JUKEBOX, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.FLOWER_POT, new BoundingBox[]{new BoundingBox((float) 0.3125, (float) 0.0, (float) 0.3125, (float) 0.6875, (float) 0.375, (float) 0.6875), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.EMERALD_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.STONE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.THIN_GLASS, new BoundingBox[]{new BoundingBox((float) 0.4375, (float) 0.0, (float) 0.0, (float) 0.5625, (float) 1.0, (float) 0.5625), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.BOOKSHELF, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.MYCEL, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.OBSIDIAN, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.PORTAL, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.GOLD_PLATE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.COAL_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.GOLD_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.STAINED_CLAY, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.MOB_SPAWNER, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.BEDROCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.IRON_ORE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.REDSTONE_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SIGN, new BoundingBox[]{new BoundingBox((float) 0.25, (float) 0.0, (float) 0.25, (float) 0.75, (float) 1.0, (float) 0.75), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.IRON_PLATE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.GOLD_ORE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.POTATO, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.25, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.MOSSY_COBBLESTONE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.RAILS, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.125, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.HAY_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.HOPPER, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.TORCH, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.ENDER_PORTAL_FRAME, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SOIL, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.9375, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.CARPET, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.0625, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.ENCHANTMENT_TABLE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.75, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DIRT, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DROPPER, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.EMERALD_ORE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.TRAPPED_CHEST, new BoundingBox[]{new BoundingBox((float) 0.0625, (float) 0.0, (float) 0.0625, (float) 0.9375, (float) 0.875, (float) 0.9375), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.REDSTONE_LAMP_ON, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.REDSTONE_LAMP_OFF, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.NETHER_WARTS, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.25, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SPONGE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.WORKBENCH, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SANDSTONE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DRAGON_EGG, new BoundingBox[]{new BoundingBox((float) 0.0625, (float) 0.0, (float) 0.0625, (float) 0.9375, (float) 1.0, (float) 0.9375), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.CAULDRON, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.CAKE, new BoundingBox[]{new BoundingBox((float) 0.0625, (float) 0.0, (float) 0.0625, (float) 0.9375, (float) 0.5, (float) 0.9375), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.LAPIS_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DIODE_BLOCK_OFF, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.125, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.NOTE_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.CACTUS, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.WOOL, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.COMMAND, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.ENDER_STONE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.TRIPWIRE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.15625, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SAPLING, new BoundingBox[]{new BoundingBox((float) 0.09999999403953552, (float) 0.0, (float) 0.09999999403953552, (float) 0.8999999761581421, (float) 0.800000011920929, (float) 0.8999999761581421), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.BED, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.5625, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.PACKED_ICE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.LAPIS_ORE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SMOOTH_BRICK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.CHEST, new BoundingBox[]{new BoundingBox((float) 0.0625, (float) 0.0, (float) 0.0625, (float) 0.9375, (float) 0.875, (float) 0.9375), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.RED_MUSHROOM, new BoundingBox[]{new BoundingBox((float) 0.30000001192092896, (float) 0.0, (float) 0.30000001192092896, (float) 0.699999988079071, (float) 0.4000000059604645, (float) 0.699999988079071), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.BROWN_MUSHROOM, new BoundingBox[]{new BoundingBox((float) 0.30000001192092896, (float) 0.0, (float) 0.30000001192092896, (float) 0.699999988079071, (float) 0.4000000059604645, (float) 0.699999988079071), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DIAMOND_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.CROPS, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.25, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.IRON_BLOCK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DIODE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.125, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.MELON, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.DIAMOND_ORE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.LEVER, new BoundingBox[]{new BoundingBox((float) 0.25, (float) 0.0, (float) 0.25, (float) 0.75, (float) 0.6000000238418579, (float) 0.75), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.SUGAR_CANE, new BoundingBox[]{new BoundingBox((float) 0.125, (float) 0.0, (float) 0.125, (float) 0.875, (float) 1.0, (float) 0.875), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.COAL_ORE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.WATER_LILY, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 0.015625, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.QUARTZ_ORE, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.GLASS, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.TRIPWIRE_HOOK, new BoundingBox[]{new BoundingBox((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0, (float) 1.0, (float) 1.0), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.WEB, new BoundingBox[]{new BoundingBox(0, 0, 0, 1, 1, 1), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.WATER, new BoundingBox[]{new BoundingBox(0, 0, 0, 1, 1, 1), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.getMaterial("STATIONARY_WATER"), new BoundingBox[]{new BoundingBox(0, 0, 0, 1, 1, 1), new BoundingBox(0, 0, 0, 0, 0, 0)});
        Arrays.stream(Material.values()).filter(material -> material.name().contains("STAIR")).forEach(material -> collisionBoundingBoxes.put(material, new BoundingBox[]{new BoundingBox(0, 0, 0, 1, 1f, 1), new BoundingBox(0, 0, 0, 0, 0, 0)}));
        collisionBoundingBoxes.put(Material.getMaterial("STATIONARY_LAVA"), new BoundingBox[]{new BoundingBox(0, 0, 0, 1, 1, 1), new BoundingBox(0, 0, 0, 0, 0, 0)});
        collisionBoundingBoxes.put(Material.BREWING_STAND, new BoundingBox[]{new BoundingBox(0.4375f, 0, 0.4375f, 0.5625f, 0.875f, 0.5625f), new BoundingBox(0, 0, 0, 1f, 0.125f, 1f)});

        Arrays.stream(Material.values()).filter(material -> material.toString().contains("FENCE") && !material.toString().contains("GATE")).forEach(material -> collisionBoundingBoxes.put(material, new BoundingBox[] {new BoundingBox(0,0,0,1,1.5f,1), new BoundingBox(0,0,0,0,0,0)}));
        Arrays.stream(Material.values()).filter(material -> material.toString().contains("STAIRS")).forEach(material -> collisionBoundingBoxes.put(material, new BoundingBox[]{new BoundingBox(0, 0, 0, 1, 1, 1), new BoundingBox(0, 0, 0, 0, 0, 0)}));
    }
}

