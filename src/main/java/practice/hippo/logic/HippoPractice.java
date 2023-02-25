package practice.hippo.logic;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class HippoPractice extends JavaPlugin implements Listener {

    public static final int VOID_LEVEL = 83;
    public static SchematicLogic schematicPaster = null;
    public HashSet<Block> recordedBlocks = new HashSet<>();
    public World world;
    public static ArrayList<String> maps = new ArrayList<>();
    public ScoreboardLogic scoreboardLogic = null;
    public HashMap<UUID, MapLogic> playerMap = new HashMap<>();

    @Override
    public void onEnable() {
        System.out.println("Hippo Practice is loading...");
        PluginManager pluginManager = this.getServer().getPluginManager();
        registerEventListeners(pluginManager);
        setDefaultGameRules();
        schematicPaster = new SchematicLogic(this, Bukkit.getWorld("world"));
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new HippoPracticeCommand(this));
        maps.add("aquatica"); maps.add("boo");
        manager.getCommandCompletions().registerCompletion("mapNames", c -> maps);
        scoreboardLogic = new ScoreboardLogic();
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

    @Override
    public void onDisable() {
        try {
            resetMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetMap() throws IOException {
        removeAllBlocksPlacedByPlayers();
        schematicPaster.loadMainBridge();
        killItems();
    }

    public void removeAllBlocksPlacedByPlayers() {
        for (Block block : recordedBlocks) {
            block.setType(Material.AIR);
        }
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
            schematicPaster.loadMap(mapName);
            Player player = (Player) sender;
            MapLogic mapLogic = new MapLogic(world, mapName, player.getUniqueId());
            playerMap.replace(player.getUniqueId(), mapLogic);
            resetPlayerAndSendToSpawn(player);
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


}
