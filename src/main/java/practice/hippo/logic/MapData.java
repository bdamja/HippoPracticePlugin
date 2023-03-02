package practice.hippo.logic;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import practice.hippo.util.BoundingBox;

public class MapData {

    @SerializedName("map_name")
    private String mapName;

    @SerializedName("map_color")
    private String mapColor;

    @SerializedName("spawn_point")
    private SpawnPoint spawnPoint;

    @SerializedName("build_limits")
    private BuildLimits buildLimits;

    @SerializedName("bridge_dimensions")
    private BridgeDimensions bridgeDimensions;

    public MapData(String mapName, String mapColor, Location spawnPointLoc, BoundingBox buildLimitsBox, BoundingBox bridgeDimensionsBox) {
        this.mapName = mapName;
        this.mapColor = mapColor;

        this.spawnPoint = new SpawnPoint();
        spawnPoint.x = spawnPointLoc.getX();
        spawnPoint.y = spawnPointLoc.getY();
        spawnPoint.z = spawnPointLoc.getZ();
        spawnPoint.yaw = spawnPointLoc.getYaw();
        spawnPoint.pitch = spawnPointLoc.getPitch();

        this.buildLimits = new BuildLimits();
        buildLimits.minX = buildLimitsBox.getMinX();
        buildLimits.minY = buildLimitsBox.getMinY();
        buildLimits.minZ = buildLimitsBox.getMinZ();
        buildLimits.maxX = buildLimitsBox.getMaxX();
        buildLimits.maxY = buildLimitsBox.getMaxY();
        buildLimits.maxZ = buildLimitsBox.getMaxZ();

        this.bridgeDimensions = new BridgeDimensions();
        bridgeDimensions.minX = bridgeDimensionsBox.getMinX();
        bridgeDimensions.minY = bridgeDimensionsBox.getMinY();
        bridgeDimensions.minZ = bridgeDimensionsBox.getMinZ();
        bridgeDimensions.maxX = bridgeDimensionsBox.getMaxX();
        bridgeDimensions.maxY = bridgeDimensionsBox.getMaxY();
        bridgeDimensions.maxZ = bridgeDimensionsBox.getMaxZ();
    }

    public MapData() {
    }

    public String getMapName() {
        return mapName;
    }

    public ChatColor getMapColor() {
        ChatColor chatColor = ChatColor.WHITE;
        switch (mapColor) {
            case "black":
                chatColor = ChatColor.BLACK;
                break;
            case "dark_blue":
                chatColor = ChatColor.DARK_BLUE;
                break;
            case "dark_green":
                chatColor = ChatColor.DARK_GREEN;
                break;
            case "dark_aqua":
                chatColor = ChatColor.DARK_AQUA;
                break;
            case "dark_red":
                chatColor = ChatColor.DARK_RED;
                break;
            case "dark_purple":
                chatColor = ChatColor.DARK_PURPLE;
                break;
            case "gold":
                chatColor = ChatColor.GOLD;
                break;
            case "gray":
                chatColor = ChatColor.GRAY;
                break;
            case "dark_gray":
                chatColor = ChatColor.DARK_GRAY;
                break;
            case "blue":
                chatColor = ChatColor.BLUE;
                break;
            case "green":
                chatColor = ChatColor.GREEN;
                break;
            case "aqua":
                chatColor = ChatColor.AQUA;
                break;
            case "red":
                chatColor = ChatColor.RED;
                break;
            case "light_purple":
                chatColor = ChatColor.LIGHT_PURPLE;
                break;
            case "yellow":
                chatColor = ChatColor.YELLOW;
                break;
        }
        return chatColor;
    }

    public String getMapText() {
        String mapNameStr = mapName.substring(0, 1).toUpperCase() + mapName.substring(1);
        return "" + getMapColor() + ChatColor.BOLD + mapNameStr;
    }

    public Location getSpawnPoint() {
        return new Location(Bukkit.getWorld("world"), spawnPoint.x, spawnPoint.y, spawnPoint.z, spawnPoint.yaw, spawnPoint.pitch);
    }

    public BoundingBox getBuildLimits() {
        return new BoundingBox(buildLimits.minX, buildLimits.minY, buildLimits.minZ, buildLimits.maxX, buildLimits.maxY, buildLimits.maxZ);
    }

    public BoundingBox getBridgeDimensions() {
        return new BoundingBox(bridgeDimensions.minX, bridgeDimensions.minY, bridgeDimensions.minZ, bridgeDimensions.maxX, bridgeDimensions.maxY, bridgeDimensions.maxZ);
    }

}

// The default color is red, so all coordinates are implied to be on the red side.
// They will automatically be flipped for players playing on the blue side.

class SpawnPoint {
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
}

class BuildLimits {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
}

class BridgeDimensions {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
}
