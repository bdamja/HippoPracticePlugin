package practice.hippo.logic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import practice.hippo.events.*;

import java.io.File;
import java.io.IOException;
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
        pluginManager.registerEvents(new WeatherChangeHandler(), this);
    }

    private static void setDefaultGameRules() {
        Bukkit.getWorld("world").setGameRuleValue("keepInventory", "true");
        Bukkit.getWorld("world").setGameRuleValue("naturalRegeneration", "false");
        Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", "false");
        Bukkit.getWorld("world").setGameRuleValue("randomTickSpeed", "0");
    }

    public void refreshPlayerAttributes(Player player) throws IOException {
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
        try {
            resetMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetMap() throws IOException {
        removeAllBlocksPlacedByPlayers();
        resetBridge();
    }

    public void removeAllBlocksPlacedByPlayers() {
        for (Block block : recordedBlocks) {
            block.setType(Material.AIR);
        }
    }

    public void resetBridge() throws IOException {
        World world = Bukkit.getWorld("world");
        pasteSchematic("bluebridge", new Location(world, 0, 92, 0, 0, 0), true);
    }

    public boolean pasteSchematic(String schematicName, Location loc, boolean noAir) throws IOException {

        File file = new File(getDataFolder() + File.separator + "schematics" + File.separator + schematicName + ".schematic");
        System.out.println(file.toPath());
        if (!file.exists()) {
            System.out.println("Could not find file");
            return false;
        }
        try {
            EditSession editSession = ClipboardFormat.;
//                    .load(file)
//                    .paste((com.sk89q.worldedit.world.World) Bukkit.getWorld("world"), BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            System.out.println("pog");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
