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
import practice.hippo.events.block.BlockPlaceHandler;
import practice.hippo.events.entity.EntityDamageHandler;
import practice.hippo.events.misc.ProjectileLaunchHandler;
import practice.hippo.events.misc.WeatherChangeHandler;
import practice.hippo.events.player.*;

import java.io.IOException;
import java.util.*;

public class HippoPractice extends JavaPlugin implements Listener {

    public static final int VOID_LEVEL = 83;
    private static final int NUM_PARTICLES = 350;

    public static SchematicLogic schematicPaster = null;
    public World world;
    public static ArrayList<String> maps = new ArrayList<>();
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
        maps.add("aquatica"); maps.add("boo");
        manager.getCommandCompletions().registerCompletion("mapNames", c -> maps);
        scoreboardLogic = new ScoreboardLogic(this);
    }

    private void registerEventListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new PlayerJoinHandler(this), this);
        pluginManager.registerEvents(new EntityDamageHandler(), this);
        pluginManager.registerEvents(new PlayerMoveHandler(this), this);
        pluginManager.registerEvents(new PlayerDropItemHandler(), this);
        pluginManager.registerEvents(new BlockPlaceHandler(this), this);
        pluginManager.registerEvents(new BlockBreakHandler(this), this);
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

    public void refreshPlayerAttributes(Player player) throws IOException {
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        InventoryLogic.setDefaultInventory(player);
    }

    public SchematicLogic getSchematicPaster() {
        return schematicPaster;
    }

    public void resetMap(Player player) throws IOException {
        removeAllBlocksPlacedByPlayer(player);
        schematicPaster.loadMainBridge();
        killItems();
        MapLogic mapLogic = playerMap.get(player.getUniqueId());
        String mapName = mapLogic.getMapName();
        MapLogic.cancelTimerTaskIfPresent(mapLogic);
        mapLogic = new MapLogic(world, mapName, player.getUniqueId(), this);
        playerMap.replace(player.getUniqueId(), mapLogic);
        mapLogic.getTimer().setStartTime();
        mapLogic.resetVisualTimer();

    }

    public void removeAllBlocksPlacedByPlayer(Player player) {
        MapLogic mapLogic = playerMap.get(player.getUniqueId());
        HashSet<Block> recordedBlocks = mapLogic.getRecordedBlocks();
        for (Block block : recordedBlocks) {
            block.setType(Material.AIR);
        }
        recordedBlocks.clear();
    }

    public HashSet<Block> getAllBlocksPlacedByPlayer(Player player) {
        MapLogic mapLogic = playerMap.get(player.getUniqueId());
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
            removeAllBlocksPlacedByPlayer(player);
            schematicPaster.loadMap(mapName);
            MapLogic.cancelTimerTaskIfPresent(playerMap.get(player.getUniqueId()));
            MapLogic mapLogic = new MapLogic(world, mapName, player.getUniqueId(), this);
            playerMap.replace(player.getUniqueId(), mapLogic);
            resetMap(player);
            resetPlayerAndSendToSpawn(player);
            scoreboardLogic.updateMapName(player, mapLogic.mapText());
        }
    }

    public void teleportToSpawnLocation(Player player) {
        MapLogic mapLogic = playerMap.get(player.getUniqueId());
        player.teleport(mapLogic.getBlueSpawnPoint());
    }

    public void teleportToViewLocation(Player player) {
        player.teleport(MapLogic.getViewLocation());
    }

    public void teleportToCenterLocation(Player player) {
        MapLogic mapLogic = playerMap.get(player.getUniqueId());
        player.teleport(mapLogic.getMapCenter());
    }

    public void completeHippo(MapLogic mapLogic, Player player) {
        long ms = mapLogic.getTimer().computeTime();
        ChatLogic.sendHippoCompletion(mapLogic, ms, player);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 0.8f);
        summonParticles(player);
        mapLogic.hasFinishedHippo = true;
        mapLogic.stopVisualTimer();
        mapLogic.updateVisualTimer(player.getScoreboard(), Timer.computeTimeFormatted(ms));
    }

    private void summonParticles(Player player) {
        double x = player.getLocation().getX();
        double y = player.getLocation().getY() + 1;
        double z = player.getLocation().getZ();
        PacketPlayOutWorldParticles particles;
        for (int i = 0; i < NUM_PARTICLES; i++) {
            Location location = getRandLocationInBox(x, y, z, 5, 3, 5, new Random());
            particles = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, (float) 255, 0, 10);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
        }
    }

    public Location getRandLocationInBox(double x, double y, double z, double xd, double yd, double zd, Random random) {
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

}
