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
import practice.hippo.events.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class PluginMain extends JavaPlugin implements Listener {

    public static final int VOID_LEVEL = 83;
    public static MapInformation currentMap = null;
    public static SchematicPaster schematicPaster = null;
    public MapInformation getCurrentMap() { return currentMap; }
    public HashSet<Block> recordedBlocks = new HashSet<>();
    private World world;
    public static ArrayList<String> maps = new ArrayList<String>();

    @Override
    public void onEnable() {
        System.out.println("Hippo Practice is loading...");
        PluginManager pluginManager = this.getServer().getPluginManager();
        registerEventListeners(pluginManager);
        setDefaultGameRules();
        currentMap = new MapInformation(Bukkit.getWorld("world"));
        schematicPaster = new SchematicPaster(this, Bukkit.getWorld("world"));
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new HippoPracticeCommand(this));
        maps.add("aquatica"); maps.add("boo");
        manager.getCommandCompletions().registerCompletion("mapNames", c -> maps);
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

    public void refreshPlayerAttributes(Player player) throws IOException {
        player.teleport(currentMap.getBlueSpawnPoint());
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        Inventory.setDefaultInventory(player);
        resetMap();
    }

    public World getWorld() {
        return world;
    }

    public SchematicPaster getSchematicPaster() {
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
        schematicPaster.loadMap(mapName);
        currentMap.updateMapValues(mapName);
        if (sender instanceof Player) {
            teleportToViewLocation((Player)sender);
        }
    }

    public void teleportToViewLocation(Player player) {
        player.teleport(MapInformation.getViewLocation());
    }

    

}
