package practice.hippo.util;

import com.sk89q.worldedit.Vector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import practice.hippo.HippoPractice;
import practice.hippo.logic.Plot;

import java.util.LinkedList;
import java.util.Queue;

public class Offset {

    public static Location location(Plot plot, Location location, boolean isBlockLocation) {
        double offsetX = location.getX() + plot.x;
        double offsetZ = location.getZ() + plot.z;
        float offsetYaw = location.getYaw();
        if (plot.getSide() == Side.blue) {
            offsetX *= -1;
            offsetZ = plot.z - (offsetZ - plot.z);
            if (!isBlockLocation) {
                offsetX += 1;
                offsetZ += 1;
            }
            offsetYaw += 180;
        }
        return new Location(location.getWorld(), offsetX, location.getY(), offsetZ, offsetYaw, location.getPitch());
    }

    public static Location location(Plot plot, World world, double x, double y, double z, boolean isBlockLocation) {
        double offsetX = x + plot.x;
        double offsetZ = z + plot.z;
        if (plot.getSide() == Side.blue) {
            offsetX *= -1;
            offsetZ = plot.z - (offsetZ - plot.z);
            if (!isBlockLocation) {
                offsetX += 1;
                offsetZ += 1;
            }
        }
        return new Location(world, offsetX, y, offsetZ);
    }

    public static Location location(Plot plot, World world, double x, double y, double z, float yaw, float pitch, boolean isBlockLocation) {
        double offsetX = x + plot.x;
        double offsetZ = z + plot.z;
        float offsetYaw = yaw;
        if (plot.getSide() == Side.blue) {
            offsetX *= -1;
            offsetZ = plot.z - (offsetZ - plot.z);
            if (!isBlockLocation) {
                offsetX += 1;
                offsetZ += 1;
            }
            offsetYaw += 180;
        }
        return new Location(world, offsetX, y, offsetZ, offsetYaw, pitch);
    }

    public static Vector worldeditVector(Plot plot, Vector vector, boolean isBlockLocation) {
        double offsetX = vector.getX() + plot.x;
        double offsetZ = vector.getZ() + plot.z;
        if (plot.getSide() == Side.blue) {
            offsetX *= -1;
            offsetZ = plot.z - (offsetZ - plot.z);
            if (!isBlockLocation) {
                offsetX += 1;
                offsetZ += 1;
            }
        }
        return new Vector(offsetX, vector.getY(), offsetZ);
    }

    public static Vector worldeditVector(Plot plot, double x, double y, double z, boolean isBlockLocation) {
        double offsetX = x + plot.x;
        double offsetZ = z + plot.z;
        if (plot.getSide() == Side.blue) {
            offsetX *= -1;
            offsetZ = plot.z - (offsetZ - plot.z);
            if (!isBlockLocation) {
                offsetX += 1;
                offsetZ += 1;
            }
        }
        return new Vector(offsetX, y, offsetZ);
    }

    public static BoundingBox boundingBox(Plot plot, BoundingBox boundingBox, boolean isBlockLocation) {
        double offsetX1 = boundingBox.getMinX() + plot.x;
        double offsetZ1 = boundingBox.getMinZ() + plot.z;
        double offsetX2 = boundingBox.getMaxX() + plot.x;
        double offsetZ2 = boundingBox.getMaxZ() + plot.z;
        if (plot.getSide() == Side.blue) {
            offsetX1 *= -1;
            offsetX2 *= -1;
            if (!isBlockLocation) {
                offsetX1 += 1;
                offsetX2 += 1;
            }
        }
        return new BoundingBox(Math.min(offsetX1, offsetX2), boundingBox.getMinY(), offsetZ1, Math.max(offsetX1, offsetX2), boundingBox.getMaxY(), offsetZ2);
    }

    public static BoundingBox boundingBox(Plot plot, double x1, double y1, double z1, double x2, double y2, double z2, boolean isBlockLocation) {
        double offsetX1 = x1 + plot.x;
        double offsetZ1 = z1 + plot.z;
        double offsetX2 = x2 + plot.x;
        double offsetZ2 = z2 + plot.z;
        if (plot.getSide() == Side.blue) {
            offsetX1 *= -1;
            offsetX2 *= -1;
            if (!isBlockLocation) {
                offsetX1 += 1;
                offsetX2 += 1;
            }
        }
        return new BoundingBox(Math.min(offsetX1, offsetX2), y1, offsetZ1, Math.max(offsetX1, offsetX2), y2, offsetZ2);
    }

    public static Queue<Location> blockQueueToOriginal(Plot plot, Queue<Block> input) {
        Queue<Block> offsetBlocks = new LinkedList<>(input);
        Queue<Location> originalBlockPositions = new LinkedList<>();
        while (!offsetBlocks.isEmpty()) {
            Block block = offsetBlocks.remove();
            int offsetX = block.getX() - (int) plot.x;
            int offsetZ = block.getZ() - (int) plot.z;
            if (plot.getSide() == Side.blue) {
                offsetX *= -1;
                offsetZ *= -1;
            }
            originalBlockPositions.offer(new Location(Bukkit.getWorld(HippoPractice.INSTANCE.worldName), offsetX, block.getY(), offsetZ));
        }
        return originalBlockPositions;
    }
}
