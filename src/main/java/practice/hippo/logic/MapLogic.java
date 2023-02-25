package practice.hippo.logic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import practice.hippo.util.BoundingBox;

import java.util.UUID;

public class MapLogic {

    private World world;
    private String mapName;
    private UUID playerUUID;
    private Location redSpawnPoint;
    private Location blueSpawnPoint;
    private BoundingBox buildLimits;
    private BoundingBox bridgeDimensions;

    public MapLogic(World world, String mapName, UUID playerUUID) {
        this.world = world;
        this.mapName = mapName;
        this.playerUUID = playerUUID;
        this.redSpawnPoint = getViewLocation();
        this.blueSpawnPoint = getViewLocation();
        this.buildLimits = new BoundingBox(0, 0, 0, 0, 0, 0);
        this.bridgeDimensions = new BoundingBox(-20, 84, -0, 20, 92, 0);
        updateMapValues(mapName);
    }

    public World getWorld() {
        return world;
    }

    public String getMapName() {
        return mapName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Location getRedSpawnPoint() {
        return redSpawnPoint;
    }

    public Location getBlueSpawnPoint() {
        return blueSpawnPoint;
    }

    public BoundingBox getBuildLimits() {
        return buildLimits;
    }

    public BoundingBox getBridgeDimensions() {
        return bridgeDimensions;
    }

    public Location getMapCenter() {
        return new Location(this.world, 0.5, 94.0, 0.5, -90, 0);
    }

    // going to change this eventually, this is just temporary
    public void updateMapValues(String mapName) {
        this.mapName = mapName;
        if (mapName.equals("aquatica")) {
            this.redSpawnPoint = new Location(Bukkit.getWorld("world"), 29.5, 98.0, 0.5, 90, 0);
            this.blueSpawnPoint = new Location(Bukkit.getWorld("world"), -28.5, 98.0, 0.5, -90, 0);
            this.buildLimits = new BoundingBox(-25, 84, -20, 0, 99, 20);
        } else if (mapName.equals("boo")) {
            this.redSpawnPoint = new Location(Bukkit.getWorld("world"), 31.5, 103.0, 0.5, 90, 0);
            this.blueSpawnPoint = new Location(Bukkit.getWorld("world"), -30.5, 103.0, 0.5, -90, 0);
            this.buildLimits = new BoundingBox(-23, 84, -20, 0, 99, 20);
        } else {
            System.out.println("this shouldnt happen lmao");
        }
    }

    public static Location getViewLocation() {
        return new Location(Bukkit.getWorld("world"), -6.5, 93, 0.5, 90, -6);
    }

}
