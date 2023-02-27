package practice.hippo.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import practice.hippo.logic.Plot;

public class OffsetLocation implements Cloneable, ConfigurationSerializable {
    private Plot plot;
    private World world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public OffsetLocation(Plot plot, World world, double x, double y, double z) {
        this(plot, world, x, y, z, 0.0F, 0.0F);
    }

    public OffsetLocation(Plot plot, World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        offset();
    }

    public void offset() {
        Location plotCenterLoc = plot.getLocation();
        
    }

    public Location toLocation() {
        return new Location(world, x, y, z);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public Chunk getChunk() {
        return this.world.getChunkAt(this.toLocation());
    }

    public Block getBlock() {
        return this.world.getBlockAt(this.toLocation());
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public int getBlockX() {
        return locToBlock(this.x);
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }

    public int getBlockY() {
        return locToBlock(this.y);
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return this.z;
    }

    public int getBlockZ() {
        return locToBlock(this.z);
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Vector getDirection() {
        Vector vector = new Vector();
        double rotX = (double)this.getYaw();
        double rotY = (double)this.getPitch();
        vector.setY(-Math.sin(Math.toRadians(rotY)));
        double xz = Math.cos(Math.toRadians(rotY));
        vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
        return vector;
    }

    public OffsetLocation setDirection(Vector vector) {
        double x = vector.getX();
        double z = vector.getZ();
        if (x == 0.0 && z == 0.0) {
            this.pitch = (float)(vector.getY() > 0.0 ? -90 : 90);
            return this;
        } else {
            double theta = Math.atan2(-x, z);
            this.yaw = (float)Math.toDegrees((theta + 6.283185307179586) % 6.283185307179586);
            double x2 = NumberConversions.square(x);
            double z2 = NumberConversions.square(z);
            double xz = Math.sqrt(x2 + z2);
            this.pitch = (float)Math.toDegrees(Math.atan(-vector.getY() / xz));
            return this;
        }
    }

    public OffsetLocation add(OffsetLocation vec) {
        if (vec != null && vec.getWorld() == this.getWorld()) {
            this.x += vec.x;
            this.y += vec.y;
            this.z += vec.z;
            return this;
        } else {
            throw new IllegalArgumentException("Cannot add OffsetLocations of differing worlds");
        }
    }

    public OffsetLocation add(Vector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    public OffsetLocation add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public OffsetLocation subtract(OffsetLocation vec) {
        if (vec != null && vec.getWorld() == this.getWorld()) {
            this.x -= vec.x;
            this.y -= vec.y;
            this.z -= vec.z;
            return this;
        } else {
            throw new IllegalArgumentException("Cannot add OffsetLocations of differing worlds");
        }
    }

    public OffsetLocation subtract(Vector vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }

    public OffsetLocation subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public double length() {
        return Math.sqrt(NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z));
    }

    public double lengthSquared() {
        return NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z);
    }

    public double distance(OffsetLocation o) {
        return Math.sqrt(this.distanceSquared(o));
    }

    public double distanceSquared(OffsetLocation o) {
        if (o == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null OffsetLocation");
        } else if (o.getWorld() != null && this.getWorld() != null) {
            if (o.getWorld() != this.getWorld()) {
                throw new IllegalArgumentException("Cannot measure distance between " + this.getWorld().getName() + " and " + o.getWorld().getName());
            } else {
                return NumberConversions.square(this.x - o.x) + NumberConversions.square(this.y - o.y) + NumberConversions.square(this.z - o.z);
            }
        } else {
            throw new IllegalArgumentException("Cannot measure distance to a null world");
        }
    }

    public OffsetLocation multiply(double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    public OffsetLocation zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            OffsetLocation other = (OffsetLocation)obj;
            if (this.world != other.world && (this.world == null || !this.world.equals(other.world))) {
                return false;
            } else if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
                return false;
            } else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
                return false;
            } else if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
                return false;
            } else if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
                return false;
            } else {
                return Float.floatToIntBits(this.yaw) == Float.floatToIntBits(other.yaw);
            }
        }
    }

    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.world != null ? this.world.hashCode() : 0);
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
        hash = 19 * hash + Float.floatToIntBits(this.pitch);
        hash = 19 * hash + Float.floatToIntBits(this.yaw);
        return hash;
    }

    public String toString() {
        return "OffsetLocation{world=" + this.world + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",pitch=" + this.pitch + ",yaw=" + this.yaw + '}';
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public OffsetLocation clone() {
        try {
            return (OffsetLocation)super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }

    public static int locToBlock(double loc) {
        return NumberConversions.floor(loc);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap();
        data.put("world", this.world.getName());
        data.put("x", this.x);
        data.put("y", this.y);
        data.put("z", this.z);
        data.put("yaw", this.yaw);
        data.put("pitch", this.pitch);
        return data;
    }
    
}
