package practice.hippo.logic;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import practice.hippo.util.BoundingBox;
import practice.hippo.util.Offset;
import practice.hippo.util.Side;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MapLogic {

    private final HippoPractice parentPlugin;
    private Plot plot;
    private World world;
    private String mapName;
    private String mapNameColor;
    private Player player;
    private Location redSpawnPoint;
    private Location blueSpawnPoint;
    private BoundingBox buildLimits;
    private BoundingBox bridgeDimensions;
    private Queue<Block> recordedBlocks;
    private ArrayList<Location> hippoBlocks;
    private Timer timer;
    private BukkitTask visualTimer;
    public boolean hasFinishedHippo;
    public boolean awaitingMove;

    public MapLogic(Plot plot, World world, String mapName, Player player, HippoPractice parentPlugin) throws FileNotFoundException {
        this.plot = plot;
        this.world = world;
        this.mapName = mapName;
        this.player = player;
        this.redSpawnPoint = getMapCenter();
        this.blueSpawnPoint = getMapCenter();
        this.buildLimits = new BoundingBox(0, 0, 0, 0, 0, 0);
        this.bridgeDimensions = new BoundingBox(-20, 84, -0, 20, 92, 0);
        this.recordedBlocks = new LinkedList<>();
        this.hasFinishedHippo = false;
        this.timer = new Timer();
        this.mapNameColor = "";
        this.parentPlugin = parentPlugin;
        this.awaitingMove = true;
        updateMapValues(mapName);
    }

    public Plot getPlot() {
        return plot;
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
    }

    public World getWorld() {
        return world;
    }

    public String getMapName() {
        return mapName;
    }

    public String getMapNameColor() {
        return mapNameColor;
    }

    public UUID getPlayerUUID() {
        return player.getUniqueId();
    }

    public Player getPlayer() {
        return player;
    }

    public Location getSpawnPoint() {
        if (this.plot.getSide() == Side.red) {
            return getRedSpawnPoint();
        } else {
            return getBlueSpawnPoint();
        }
    }

    public Location getRedSpawnPoint() {
        return Offset.location(this.plot, redSpawnPoint, false);
    }

    public Location getBlueSpawnPoint() {
        return Offset.location(this.plot, blueSpawnPoint, false);
    }

    public BoundingBox getBuildLimits() {
        return Offset.boundingBox(this.plot, buildLimits, true);
    }

    public BoundingBox getBridgeDimensions() {
        return Offset.boundingBox(this.plot, bridgeDimensions, true);
    }

    public Queue<Block> getRecordedBlocks() {
        return recordedBlocks;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public BukkitTask getVisualTimer() {
        return this.visualTimer;
    }

    public Location getMapCenter() {
        return new Location(this.world, 2.5, 93.0, 0.5, -90, 0);
    }

    private ArrayList<Location> getLocationFromHippoFile(String mapName) throws FileNotFoundException {
        File file = new File("./plugins/HippoPractice/hippos/" + mapName + ".txt");
        Scanner input = new Scanner(file);
        ArrayList<Location> allHippoBlocks = new ArrayList<>();
        while (input.hasNext()) {
            String line = input.nextLine();
            String[] coords = line.split(" ");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            int z = Integer.parseInt(coords[2]);
            allHippoBlocks.add(Offset.location(this.plot, this.world, x, y, z, true));
        }
        input.close();
        return allHippoBlocks;
    }

    // going to change this eventually, this is just temporary
    public void updateMapValues(String mapName) throws FileNotFoundException {
        this.mapName = mapName;
        if (mapName.equals("aquatica")) {
            this.redSpawnPoint = new Location(this.world, 29.5, 98.0, 0.5, 90, 0);
            this.blueSpawnPoint = redSpawnPoint;
            this.buildLimits = new BoundingBox(25, 84, -20, 1, 99, 20);
            mapNameColor = "" + ChatColor.DARK_AQUA;

        } else if (mapName.equals("boo")) {
            this.redSpawnPoint = new Location(this.world, 31.5, 103.0, 0.5, 90, 0);
            this.blueSpawnPoint = redSpawnPoint;
            this.buildLimits = new BoundingBox(23, 84, -20, 1, 99, 20);
            mapNameColor = "" + ChatColor.DARK_PURPLE;
        }
        this.hippoBlocks = getLocationFromHippoFile(mapName);
    }

    public String mapText() {
        String mapNameStr = mapName.substring(0, 1).toUpperCase() + mapName.substring(1);
        return this.mapNameColor + ChatColor.BOLD + mapNameStr + ChatColor.RESET;
    }

    public ArrayList<Location> getHippoBlocks() {
        return hippoBlocks;
    }

    public boolean isBlockLocationPartOfHippo(Location location) throws FileNotFoundException {
        ArrayList<Location> allHippoBlocks = getLocationFromHippoFile(mapName);
        return allHippoBlocks.contains(location);
    }

    public Location getViewLocation() {
        return new Location(this.world, 6.5, 93, 0.5, 90, -6);
    }

    public void resetVisualTimer() {
        stopVisualTimer();
        Scoreboard board = player.getScoreboard();
        board.getTeam("timeName").setPrefix(ChatColor.GRAY + "0.000");
        startVisualTimer(board);
    }

    public void startVisualTimer(Scoreboard board) {
        this.visualTimer = new BukkitRunnable() {
            @Override
            public void run() {
                updateVisualTimer(board, Timer.computeTimeFormatted(timer.computeTime()));
            }
        }.runTaskTimer(parentPlugin, 0, 3);
    }

    public void stopVisualTimer() {
        if (visualTimer != null) {
            visualTimer.cancel();
        }
    }

    public void updateVisualTimer(Scoreboard board, String timeFormatted) {
        board.getTeam("timeName").setPrefix(ChatColor.GRAY + timeFormatted);
    }

    public static void cancelTimerTaskIfPresent(MapLogic mapLogic) {
        if (mapLogic != null) {
            if (mapLogic.getVisualTimer() != null) {
                mapLogic.getVisualTimer().cancel();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void placeHippoBlocks(World world) throws FileNotFoundException {
        byte color = (this.plot.getSide() == Side.red) ? (byte) 14 : (byte) 11;
        parentPlugin.removeAllBlocksPlacedByPlayer(player);
        ArrayList<Location> allHippoBlocks = getLocationFromHippoFile(this.mapName);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!allHippoBlocks.isEmpty()) {
                    Location location = allHippoBlocks.remove(0);
                    Block block = world.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                    block.setType(Material.STAINED_CLAY);
                    block.setData(color);
                    block.setMetadata("placed by " + player.getName(), new FixedMetadataValue(parentPlugin, ""));
                    parentPlugin.playerMap.get(getPlayerUUID()).getRecordedBlocks().add(block);
                    player.playSound(block.getLocation(), Sound.DIG_STONE, 1.0f, 1.0f);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(parentPlugin, 3, 3);
    }

}
