package practice.hippo.logic;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import practice.hippo.events.*;

import java.util.HashSet;

public class PluginMain extends JavaPlugin implements Listener {

    public static final int VOID_LEVEL = 83;
    public static MapInformation currentMap = null;
    public MapInformation getCurrentMap() { return currentMap; }
    public HashSet<Block> recordedBlocks = new HashSet<>();

    @Override
    public void onEnable() {
        System.out.println("Hippo Practice is loading...");
        PluginManager pluginManager = this.getServer().getPluginManager();
        registerEventListeners(pluginManager);
        setDefaultGameRules();
        currentMap = new MapInformation();
    }

    private void registerEventListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new PlayerJoinHandler(this), this);
        pluginManager.registerEvents(new EntityDamageHandler(), this);
        pluginManager.registerEvents(new PlayerMoveHandler(this), this);
        pluginManager.registerEvents(new PlayerDropItemHandler(), this);
        pluginManager.registerEvents(new BlockPlaceHandler(this), this);
        pluginManager.registerEvents(new BlockBreakHandler(this), this);
        pluginManager.registerEvents(new PlayerChatHandler(), this);
    }

    private static void setDefaultGameRules() {
        Bukkit.getWorld("world").setGameRuleValue("keepInventory", "true");
        Bukkit.getWorld("world").setGameRuleValue("naturalRegeneration", "false");
        Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", "false");
        Bukkit.getWorld("world").setGameRuleValue("randomTickSpeed", "0");
    }

    public void refreshPlayerAttributes(Player player) {
        player.teleport(MapInformation.getMapCenter());
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        Inventory.setDefaultInventory(player);
        resetMap();
    }

    @Override
    public void onDisable() {
        resetMap();
    }

    public void resetMap() {
        removeAllBlocksPlacedByPlayers();
        resetBridge();
    }

    public void removeAllBlocksPlacedByPlayers() {
        for (Block block : recordedBlocks) {
            block.setType(Material.AIR);
        }
    }

    public void resetBridge() {

    }

}
