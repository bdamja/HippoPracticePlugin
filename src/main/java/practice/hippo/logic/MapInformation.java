package practice.hippo.logic;

import javafx.geometry.BoundingBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class MapInformation {

    private final static World world = Bukkit.getWorld("world");

    private String mapName;
    private Location redSpawnPoint;
    private Location blueSpawnPoint;
    private BoundingBox buildLimits;
    private BoundingBox bridgeDimensions;

    public MapInformation() {
        this.mapName = "Aquatica";
        this.redSpawnPoint = new Location(world, 29.5, 98.0, 0.5, 90, 0);
        this.blueSpawnPoint = new Location(world, -28.5, 98.0, 0.5, -90, 0);
        this.buildLimits = new BoundingBox(-25.0, 84.0, -20.0, 50, 15, 40);
        this.bridgeDimensions = new BoundingBox(-20.0, 84, 0.0, 40.0, 8.0, 0.0);
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

    public static Location getMapCenter() {
        return new Location(world, 0.5, 94.0, 0.5, -90, 0);
    }

}
