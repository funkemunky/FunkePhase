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

package cc.funkemunky.funkephase.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MathUtils {

    private static String spigot_id = "%%__USER__%%";

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static long elapsed(long starttime) {
        return System.currentTimeMillis() - starttime;
    }

    public static double trim(int degree, double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = String.valueOf(format) + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d).replaceAll(",", "."));
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double offset(final Vector one, final Vector two) {
        return one.subtract(two).length();
    }

    public static double getHorizontalDistance(Location to, Location from) {
        double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));

        return Math.sqrt(x * x + z * z);
    }

    public static float[] getRotations(Location one, Location two) {
        double diffX = two.getX() - one.getX();
        double diffZ = two.getZ() - one.getZ();
        double diffY = (two.getY() + 2.0D - 0.4D) - (
                one.getY() + 2.0D);
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

        return new float[]{yaw, pitch};
    }

    public static double[] getOffsetFromEntity(Player player, LivingEntity entity) {
        double yaw = getRotations(player.getLocation(), entity.getLocation())[0];
        double pitch = getRotations(player.getLocation(), entity.getLocation())[1];

        double yawOffset = Math.abs(yaw - player.getLocation().getYaw());
        double pitchOffset = Math.abs(pitch - player.getLocation().getPitch());

        return new double[]{yawOffset, pitchOffset};
    }

    public static double[] getOffsetFromLocation(Location one, Location two) {
        double yaw = getRotations(one, two)[0];
        double pitch = getRotations(one, two)[1];

        double yawOffset = Math.abs(yaw - one.getYaw());
        double pitchOffset = Math.abs(pitch - one.getPitch());

        return new double[]{yawOffset, pitchOffset};
    }

    public static double getDifference(double one, double... two) {
        double outcome = one;

        for (double second : two) {
            outcome -= second;
        }

        return outcome;
    }

    public static double getYawDifference(Location one, Location two) {
        return Math.abs(one.getYaw() - two.getYaw());
    }

    public static double getVerticalDistance(Location to, Location from) {
        double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));

        return Math.sqrt(y * y);
    }

    public static double getYDifference(Location to, Location from) {
        return Math.abs(to.getY() - from.getY());
    }

    public static Vector getHorizontalVector(Location loc) {
        Vector vec = loc.toVector();
        vec.setY(0);
        return vec;
    }

    public static int floor(double var0) {
        int var2 = (int)var0;
        return var0 < (double)var2 ? var2 - 1 : var2;
    }

    public static Vector getVerticalVector(Location loc) {
        Vector vec = loc.toVector();
        vec.setX(0);
        vec.setZ(0);
        return vec;
    }

    public static float[] getRotation2(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        dy /= distanceXZ;
        float yaw = (float) (-(Math.atan2(dx, dz) * 57.29577951308232));
        float pitch = (float) (-(Math.asin(dy) * 57.29577951308232));
        return new float[]{yaw, pitch};
    }

    public static float yawTo180F(float flub) {
        flub %= 360.0F;
        if (flub >= 180.0F) {
            flub -= 360.0F;
        }
        if (flub < -180.0F) {
            flub += 360.0F;
        }
        return flub;
    }

    public static double yawTo180D(double dub) {
        dub %= 360.0D;
        if (dub >= 180.0D) {
            dub -= 360.0D;
        }
        if (dub < -180.0D) {
            dub += 360.0D;
        }
        return dub;
    }
}
