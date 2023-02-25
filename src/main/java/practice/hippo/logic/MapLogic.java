package practice.hippo.logic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import practice.hippo.util.BoundingBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.UUID;

public class MapLogic {

    private World world;
    private String mapName;
    private UUID playerUUID;
    private Location redSpawnPoint;
    private Location blueSpawnPoint;
    private BoundingBox buildLimits;
    private BoundingBox bridgeDimensions;
    private HashSet<Block> recordedBlocks;
    private ArrayList<Location> hippoBlocks;
    public boolean hasFinishedHippo;

    public MapLogic(World world, String mapName, UUID playerUUID) throws FileNotFoundException {
        this.world = world;
        this.mapName = mapName;
        this.playerUUID = playerUUID;
        this.redSpawnPoint = getViewLocation();
        this.blueSpawnPoint = getViewLocation();
        this.buildLimits = new BoundingBox(0, 0, 0, 0, 0, 0);
        this.bridgeDimensions = new BoundingBox(-20, 84, -0, 20, 92, 0);
        this.recordedBlocks = new HashSet<>();
        this.hasFinishedHippo = false;
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

    public HashSet<Block> getRecordedBlocks() {
        return recordedBlocks;
    }

    public Location getMapCenter() {
        return new Location(this.world, 0.5, 94.0, 0.5, -90, 0);
    }

    private ArrayList<Location> setHippoBlocks(String mapName) throws FileNotFoundException {
        File file = new File("./plugins/HippoPractice/hippos/" + mapName + ".txt");
        Scanner input = new Scanner(file);
        ArrayList<Location> allHippoBlocks = new ArrayList<>();
        while (input.hasNext()) {
            String line = input.nextLine();
            String[] coords = line.split(" ");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            int z = Integer.parseInt(coords[2]);
            allHippoBlocks.add(new Location(world, x, y, z));
        }
        input.close();
        return allHippoBlocks;
    }

    // going to change this eventually, this is just temporary
    public void updateMapValues(String mapName) throws FileNotFoundException {
        this.mapName = mapName;
        if (mapName.equals("aquatica")) {
            this.redSpawnPoint = new Location(Bukkit.getWorld("world"), 29.5, 98.0, 0.5, 90, 0);
            this.blueSpawnPoint = new Location(Bukkit.getWorld("world"), -28.5, 98.0, 0.5, -90, 0);
            this.buildLimits = new BoundingBox(-25, 84, -20, 0, 99, 20);
        } else if (mapName.equals("boo")) {
            this.redSpawnPoint = new Location(Bukkit.getWorld("world"), 31.5, 103.0, 0.5, 90, 0);
            this.blueSpawnPoint = new Location(Bukkit.getWorld("world"), -30.5, 103.0, 0.5, -90, 0);
            this.buildLimits = new BoundingBox(-23, 84, -20, 0, 99, 20);
        }
        this.hippoBlocks = setHippoBlocks(mapName);
    }

    public ArrayList<Location> getHippoBlocks() {
        return hippoBlocks;
    }

    public static Location getViewLocation() {
        return new Location(Bukkit.getWorld("world"), -6.5, 93, 0.5, 90, -6);
    }

}
