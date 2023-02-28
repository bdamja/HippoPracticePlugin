package practice.hippo.logic;

import co.aikar.commands.PaperCommandManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import practice.hippo.commands.HippoPracticeCommand;
import practice.hippo.events.block.BlockBreakHandler;
import practice.hippo.events.block.BlockDamageHandler;
import practice.hippo.events.block.BlockPlaceHandler;
import practice.hippo.events.entity.EntityDamageHandler;
import practice.hippo.events.misc.ProjectileLaunchHandler;
import practice.hippo.events.misc.WeatherChangeHandler;
import practice.hippo.events.player.*;
import practice.hippo.util.Offset;

import java.io.IOException;
import java.util.*;

import static practice.hippo.util.Side.blue;
import static practice.hippo.util.Side.red;

public class HippoPractice extends JavaPlugin implements Listener {

    public static final int VOID_LEVEL = 83;
    private static final int NUM_PARTICLES = 350;

    public static SchematicLogic schematicPaster = null;
    public World world;
    public static Queue<String> maps = new LinkedList<>();
    private static final ArrayList<Plot> plots = new ArrayList<>();
    public ScoreboardLogic scoreboardLogic = null;
    public HashMap<UUID, MapLogic> playerMap = new HashMap<>();

    @Override
    public void onEnable() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        registerEventListeners(pluginManager);
        setDefaultGameRules();
        schematicPaster = new SchematicLogic(this, Bukkit.getWorld("world"));
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new HippoPracticeCommand(this));
        maps.add("aquatica"); maps.add("boo"); maps.add("chronon"); maps.add("condo"); maps.add("dojo"); maps.add("fortress"); maps.add("galaxy"); maps.add("sorcery"); maps.add("treehouse"); maps.add("urban");
        manager.getCommandCompletions().registerCompletion("mapNames", c -> maps);
        scoreboardLogic = new ScoreboardLogic(this);
        setPlotList();
    }

    private void registerEventListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new PlayerJoinHandler(this), this);
        pluginManager.registerEvents(new EntityDamageHandler(), this);
        pluginManager.registerEvents(new PlayerMoveHandler(this), this);
        pluginManager.registerEvents(new PlayerDropItemHandler(), this);
        pluginManager.registerEvents(new BlockPlaceHandler(this), this);
        pluginManager.registerEvents(new BlockBreakHandler(this), this);
        pluginManager.registerEvents(new BlockDamageHandler(this), this);
        pluginManager.registerEvents(new PlayerRespawnHandler(this), this);
        pluginManager.registerEvents(new ProjectileLaunchHandler(this), this);
        pluginManager.registerEvents(new PlayerChatHandler(), this);
        pluginManager.registerEvents(new WeatherChangeHandler(), this);
    }

    private void setDefaultGameRules() {
        world = Bukkit.getWorld("world");
        world.setGameRuleValue("keepInventory", "true");
        world.setGameRuleValue("naturalRegeneration", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("randomTickSpeed", "0");
    }

    public void resetPlayerAndSendToSpawn(Player player) throws IOException {
        teleportToSpawnLocation(player);
        refreshPlayerAttributes(player);
    }

    public void resetPlayerAndSendToView(Player player) throws IOException {
        teleportToViewLocation(player);
        refreshPlayerAttributes(player);
    }

    public void resetPlayerAndSendToCenter(Player player) throws IOException {
        teleportToCenterLocation(player);
        refreshPlayerAttributes(player);
    }

    public void refreshPlayerAttributes(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        InventoryLogic.setDefaultInventory(player, getMapLogic(player).getPlot().getSide());
    }

    public SchematicLogic getSchematicPaster() {
        return schematicPaster;
    }

    public void resetMap(Player player) throws IOException {
        MapLogic mapLogic = getMapLogic(player);
        Plot plot = mapLogic.getPlot();
        String mapName = mapLogic.getMapName();
        removeAllBlocksPlacedByPlayer(player);
        killItems();
        schematicPaster.loadMainBridge(plot);
        MapLogic.cancelTasksIfPresent(mapLogic);
        mapLogic = new MapLogic(plot, world, mapName, player, this);
        playerMap.replace(player.getUniqueId(), mapLogic);
        mapLogic.resetVisualTimer();
    }

    public void removeAllBlocksPlacedByPlayer(Player player) {
        MapLogic mapLogic = getMapLogic(player);
        if (mapLogic != null) {
            Queue<Block> recordedBlocks = mapLogic.getRecordedBlocks();
            for (Block block : recordedBlocks) {
                block.setType(Material.AIR);
            }
            recordedBlocks.clear();
        }
    }

    public Queue<Block> getAllBlocksPlacedByPlayer(Player player) {
        MapLogic mapLogic = getMapLogic(player);
        return mapLogic.getRecordedBlocks();
    }

    private void killItems() {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Item) {
                entity.remove();
            }
        }
    }

    public void changeMap(String mapName, CommandSender sender) throws IOException {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            MapLogic mapLogic = getMapLogic(player);
            Plot plot = mapLogic.getPlot();
            removeAllBlocksPlacedByPlayer(player);
            MapLogic.cancelTasksIfPresent(getMapLogic(player));
            mapLogic = new MapLogic(getMapLogic(player).getPlot(), world, mapName, player, this);
            playerMap.replace(player.getUniqueId(), mapLogic);
            schematicPaster.loadMap(plot, mapLogic.getMapName());
            resetMap(player);
            resetPlayerAndSendToSpawn(player);
            scoreboardLogic.updateMapName(player, mapLogic.mapText());
        }
    }

    public void teleportToSpawnLocation(Player player) {
        MapLogic mapLogic = getMapLogic(player);
        player.teleport(mapLogic.getSpawnPoint());
    }

    public void teleportToViewLocation(Player player) {
        MapLogic mapLogic = getMapLogic(player);
        player.teleport(Offset.location(mapLogic.getPlot(), mapLogic.getViewLocation(), false));
    }

    public void teleportToCenterLocation(Player player) {
        MapLogic mapLogic = getMapLogic(player);
        player.teleport(Offset.location(mapLogic.getPlot(), mapLogic.getMapCenter(), false));
    }

    public void completeHippo(MapLogic mapLogic, Player player) {
        long ms = mapLogic.getTimer().computeTime();
        ChatLogic.sendHippoCompletion(mapLogic, ms, player);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 0.8f);
        summonCompletionParticles(player);
        mapLogic.hasFinishedHippo = true;
        mapLogic.stopVisualTimer();
        mapLogic.updateVisualTimer(player.getScoreboard(), Timer.computeTimeFormatted(ms));
    }

    private void summonCompletionParticles(Player player) {
        double x = player.getLocation().getX();
        double y = player.getLocation().getY() + 1;
        double z = player.getLocation().getZ();
        PacketPlayOutWorldParticles particles;
        Plot plot = getMapLogic(player).getPlot();
        for (int i = 0; i < NUM_PARTICLES; i++) {
            Location location = getRandLocationInBox(plot, x, y, z, 5, 3, 5, new Random());
            particles = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true,
                    (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                    0, 0, 0, (float) 255, 0, 10);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
        }
    }

    public Location getRandLocationInBox(Plot plot, double x, double y, double z, double xd, double yd, double zd, Random random) {
        double minX = x - xd;
        double minY = y - yd;
        double minZ = z - zd;
        double maxX = x + xd;
        double maxY = y + yd;
        double maxZ = z + zd;
        double randomX = minX + (maxX - minX) * random.nextDouble();
        double randomY = minY + (maxY - minY) * random.nextDouble();
        double randomZ = minZ + (maxZ - minZ) * random.nextDouble();
        return new Location(world, randomX, randomY, randomZ);
    }

    public final void setPlotList() {
        plots.add(new Plot(this, new Location(world, 0, 0, 0), red));
        plots.add(new Plot(this, new Location(world, 0, 0, 41), red));
        plots.add(new Plot(this, new Location(world, 0, 0, 82), red));
        plots.add(new Plot(this, new Location(world, 0, 0, 123), red));
        plots.add(new Plot(this, new Location(world, 0, 0, 164), red));
        plots.add(new Plot(this, new Location(world, 0, 0, 0), blue));
        plots.add(new Plot(this, new Location(world, 0, 0, 41), blue));
        plots.add(new Plot(this, new Location(world, 0, 0, 82), blue));
        plots.add(new Plot(this, new Location(world, 0, 0, 123), blue));
        plots.add(new Plot(this, new Location(world, 0, 0, 164), blue));
    }

    public ArrayList<Plot> getPlotList() {
        return plots;
    }

    public MapLogic getMapLogic(Player player) {
        return playerMap.get(player.getUniqueId());
    }

    public boolean isPlotOccupied(Plot plot) {
        boolean isOccupied = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            MapLogic mapLogic = getMapLogic(player);
            if (mapLogic != null) {
                Plot occupiedPlot = mapLogic.getPlot();
                if (occupiedPlot != null) {
                    if (occupiedPlot.z == plot.z && occupiedPlot.getSide() == plot.getSide()) {
                        isOccupied = true;
                    }
                }
            }
        }
        return isOccupied;
    }

    @SuppressWarnings("deprecation")
    public void showMissingBlocks(MapLogic mapLogic) {
        for (Block block : mapLogic.getRecordedBlocks()) {
            block.setType(Material.STAINED_GLASS);
            block.setData(mapLogic.getColorData());
        }
        mapLogic.awaitingLeftClick = true;
        showMissingParticles(mapLogic);
    }

    @SuppressWarnings("deprecation")
    public void revertGlassToClay(MapLogic mapLogic) {
        for (Block block : mapLogic.getRecordedBlocks()) {
            block.setType(Material.STAINED_CLAY);
            block.setData(mapLogic.getColorData());
        }
        mapLogic.awaitingLeftClick = false;
        mapLogic.stopParticleSummoning();
    }

    private void showMissingParticles(MapLogic mapLogic) {
        ArrayList<Location> missingParticleLocations = getMissingBlockLocations(mapLogic);
        mapLogic.startParticleSummoning(missingParticleLocations, 25);
    }

    private ArrayList<Location> getMissingBlockLocations(MapLogic mapLogic) {
        ArrayList<Location> missingBlockLocations = new ArrayList<>();
        for (Location location : mapLogic.getHippoBlocks()) {
            if (world.getBlockAt(location).getType().equals(Material.AIR)) {
                double centerX = location.getX() + 0.5;
                double centerY = location.getY() + 0.5;
                double centerZ = location.getZ() + 0.5;
                missingBlockLocations.add(new Location(world, centerX, centerY, centerZ));
            }
        }
        return missingBlockLocations;
    }

}
