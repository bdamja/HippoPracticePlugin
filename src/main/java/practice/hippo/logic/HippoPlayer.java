package practice.hippo.logic;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import practice.hippo.hippodata.HippoData;
import practice.hippo.mapdata.MapData;
import practice.hippo.playerdata.PlayerData;
import practice.hippo.util.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class HippoPlayer {

    private final HippoPractice parentPlugin;
    private Plot plot;
    private World world;
    private MapData mapData;
    private Player player;
    private PlayerData playerData;
    private HippoData hippoData;
    private Queue<Block> recordedBlocks;
    private ArrayList<Location> hippoBlocks;
    private Timer timer;
    private BukkitTask visualTimer;
    private BukkitTask particleSummoner;
    public boolean hasFinishedHippo;
    public boolean awaitingMove;
    public boolean awaitingLeftClick;
    public boolean isEditingKit;

    public HippoPlayer(Plot plot, World world, String mapName, Player player, HippoPractice parentPlugin) throws FileNotFoundException {
        this.plot = plot;
        this.world = world;
        this.mapData = new MapData();
        this.player = player;
        this.playerData = new PlayerData(parentPlugin, player.getName(), player.getUniqueId().toString());
        this.hippoData = null;
        this.recordedBlocks = new LinkedList<>();
        this.hasFinishedHippo = false;
        this.timer = new Timer();
        timer.setStartTime();
        this.parentPlugin = parentPlugin;
        this.awaitingMove = true;
        this.awaitingLeftClick = false;
        this.isEditingKit = false;
        this.hippoBlocks = getLocationFromHippoFile(mapName);
        readMapData(mapName);
    }

    private void readMapData(String mapName) throws FileNotFoundException {
        if (!mapName.equals("no_map")) {
            if (HippoPractice.USE_DATABASE) {
                mapData = MongoDB.getMapDataFromDocument(mapName);
            } else {
                File mapDataFile = new File(parentPlugin.getPluginsDirSubdir("mapdata") + File.separator + mapName + ".json");
                if (mapDataFile.exists()) {
                    Gson gson = new Gson();
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(mapDataFile));
                    mapData = gson.fromJson(bufferedReader, MapData.class);
                } else {
                    parentPlugin.getLogger().severe("Error when trying to load map data: Could not find file: " + mapDataFile);
                }
            }
        }
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

    public MapData getMapData() {
        return mapData;
    }

    public String getMapName() {
        return mapData.getMapName();
    }

    public ChatColor getMapColor() {
        return mapData.getMapColor();
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
        return Offset.location(this.plot, mapData.getSpawnPoint(), false);
    }

    public Location getBlueSpawnPoint() {
        return Offset.location(this.plot, mapData.getSpawnPoint(), false);
    }

    public BoundingBox getBuildLimits() {
        return Offset.boundingBox(this.plot, mapData.getBuildLimits(), true);
    }

    public ArrayList<BoundingBox> getBlacklistedRegions() {
        BoundingBox[] blacklistedRegions = mapData.getBlacklistedRegions();
        ArrayList<BoundingBox> regions = new ArrayList<>();
        if (blacklistedRegions != null) {
            for (BoundingBox region : blacklistedRegions) {
                regions.add(Offset.boundingBox(this.plot, region, true));
            }
        }
        return regions;
    }

    public BoundingBox getBridgeDimensions() {
        return Offset.boundingBox(this.plot, mapData.getBridgeDimensions(), true);
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

    public BukkitTask getParticleSummoner() {
        return this.particleSummoner;
    }

    public Location getMapCenter() {
        return new Location(this.world, 2.5, 93.0, 0.5, -90, 0);
    }

    private ArrayList<Location> getLocationFromHippoFile(String mapName) throws FileNotFoundException {
        if (HippoPractice.USE_DATABASE) {
            hippoData = MongoDB.getHippoDataFromDocument(mapName);
        } else {
            File file = new File(parentPlugin.getPluginsDirSubdir("hippodata") + File.separator + mapName + ".json");
            if (file.exists()) {
                Gson gson = new Gson();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                hippoData = gson.fromJson(bufferedReader, HippoData.class);
            } else {
                parentPlugin.getLogger().severe("Error when trying to load hippo data: Could not find file: " + file);
            }
        }

        ArrayList<Location> allHippoBlocks = new ArrayList<>();
        if (hippoData != null) {
            for (Vector vector : hippoData.getBlocks()) {
                allHippoBlocks.add(Offset.location(this.plot, this.world, vector.getX(), vector.getY(), vector.getZ(), true));
                System.out.println(vector);
            }
        }
        return allHippoBlocks;
    }
    
    public void updateMapValues(String mapName) throws FileNotFoundException {
        readMapData(mapName);
    }

    public String mapText() {
        return mapData.getMapText();
    }

    public ArrayList<Location> getHippoBlocks() {
        return hippoBlocks;
    }

    public boolean isBlockLocationPartOfHippo(Location location) throws FileNotFoundException {
        ArrayList<Location> allHippoBlocks = getLocationFromHippoFile(mapData.getMapName());
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

    public void startParticleSummoning(ArrayList<Location> missingParticleLocations, long period) {
        stopParticleSummoning();
        this.particleSummoner = new BukkitRunnable() {
            @Override
            public void run() {
                for (Location location : missingParticleLocations) {
                    PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.BARRIER, true,
                            (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                            0, 0, 0, (float) 255, 0, 10);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
                }
            }
        }.runTaskTimer(parentPlugin, 0, period);
    }

    public void stopParticleSummoning() {
        if (particleSummoner != null) {
            particleSummoner.cancel();
        }
    }

    public void updateVisualTimer(Scoreboard board, String timeFormatted) {
        board.getTeam("timeName").setPrefix(ChatColor.GRAY + timeFormatted);
    }

    public static void cancelTasksIfPresent(HippoPlayer hippoPlayer) {
        if (hippoPlayer != null) {
            if (hippoPlayer.getVisualTimer() != null) {
                hippoPlayer.getVisualTimer().cancel();
            }
            if (hippoPlayer.getParticleSummoner() != null) {
                hippoPlayer.getParticleSummoner().cancel();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void placeHippoBlocks(World world) throws FileNotFoundException {
        ArrayList<Location> allHippoBlocks = getLocationFromHippoFile(mapData.getMapName());
        if (!allHippoBlocks.isEmpty()) {
            byte color = getColorData();
            parentPlugin.removeAllBlocksPlacedByPlayer(player);
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
        } else {
            ChatLogic.sendMessageToPlayer(ChatColor.RED + "There is no hippo set for this map.", player);
        }
    }

    public byte getColorData() {
        return (this.plot.getSide() == Side.red) ? (byte) 14 : (byte) 11;
    }

    public String getBiome() {
        if (this.plot.getSide() == Side.red) {
            return mapData.getBiomeRed();
        } else {
            return mapData.getBiomeBlue();
        }
    }

    public int getBiomeId() {
        return BiomeType.idOf(getBiome());
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }
}
