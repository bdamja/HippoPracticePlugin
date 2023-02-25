package practice.hippo.logic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import practice.hippo.util.BoundingBox;

public class MapInformation {

    private World world;
    private String mapName;
    private Location redSpawnPoint;
    private Location blueSpawnPoint;
    private BoundingBox buildLimits;
    private BoundingBox bridgeDimensions;

    public MapInformation(World world) {
        this.world = world;
        this.mapName = "Aquatica";
        this.redSpawnPoint = new Location(Bukkit.getWorld("world"), 29.5, 98.0, 0.5, 90, 0);
        this.blueSpawnPoint = new Location(Bukkit.getWorld("world"), -28.5, 98.0, 0.5, -90, 0);
        this.buildLimits = new BoundingBox(-25, 84, -20, 25, 99, 20);
        this.bridgeDimensions = new BoundingBox(-20, 84, -0, 20, 92, 0);
    }

    public Location getRedSpawnPoint() {
        return this.redSpawnPoint;
    }

    public Location getBlueSpawnPoint() {
        return this.blueSpawnPoint;
    }

    public BoundingBox getBuildLimits() {
        return this.buildLimits;
    }

    public BoundingBox getBridgeDimensions() {
        return this.bridgeDimensions;
    }

    public Location getMapCenter() {
        return new Location(this.world, 0.5, 94.0, 0.5, -90, 0);
    }

}
