package practice.hippo.mapdata;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import practice.hippo.HippoPractice;
import practice.hippo.logic.ChatLogic;
import practice.hippo.util.BoundingBox;

public class MapData {

    @SerializedName("map_name")
    private String mapName = "null";

    @SerializedName("map_color")
    private String mapColor = "null";

    @SerializedName("biome_red")
    private String biomeRed = "null";

    @SerializedName("biome_blue")
    private String biomeBlue = "null";

    @SerializedName("spawn_point")
    private SpawnPoint spawnPoint;

    @SerializedName("build_limits")
    private BuildLimits buildLimits;

    @SerializedName("bridge_dimensions")
    private BridgeDimensions bridgeDimensions;

    @SerializedName("blacklisted_regions")
    private BoundingBox[] blacklistedRegions;

    public MapData() {
    }

    public String getMapName() {
        return mapName;
    }

    public ChatColor getMapColor() {
        return ChatLogic.strToChatColor(mapColor);
    }

    public String getMapText() {
        String mapNameStr = "";
        String[] words = mapName.split("_");
        for (int i = 0; i < words.length; i++) {
            String space = (i == 0) ? "" : " ";
            mapNameStr = mapNameStr.concat(space + words[i].substring(0, 1).toUpperCase() + words[i].substring(1));
        }
        return "" + getMapColor() + mapNameStr;
    }

    public String getBiomeRed() {
        return biomeRed;
    }

    public String getBiomeBlue() {
        return biomeBlue;
    }

    public Location getSpawnPoint() {
        return new Location(Bukkit.getWorld(HippoPractice.INSTANCE.worldName), spawnPoint.x, spawnPoint.y, spawnPoint.z, spawnPoint.yaw, spawnPoint.pitch);
    }

    public BoundingBox getBuildLimits() {
        return new BoundingBox(buildLimits.minX, buildLimits.minY, buildLimits.minZ, buildLimits.maxX, buildLimits.maxY, buildLimits.maxZ);
    }

    public BoundingBox getBridgeDimensions() {
        return new BoundingBox(bridgeDimensions.minX, bridgeDimensions.minY, bridgeDimensions.minZ, bridgeDimensions.maxX, bridgeDimensions.maxY, bridgeDimensions.maxZ);
    }

    public int getBridgeLength() {
        return (int) (Math.abs(bridgeDimensions.minX) + Math.abs(bridgeDimensions.maxX) + 1);
    }

    public BoundingBox[] getBlacklistedRegions() {
        return blacklistedRegions;
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
