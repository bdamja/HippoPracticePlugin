package practice.hippo.logic;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Location;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import practice.hippo.events.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class PluginMain extends JavaPlugin implements Listener {

    public static final int VOID_LEVEL = 83;
    public static MapInformation currentMap = null;
    public MapInformation getCurrentMap() { return currentMap; }
    public HashSet<Block> recordedBlocks = new HashSet<>();
    private World world;

    @Override
    public void onEnable() {
        System.out.println("Hippo Practice is loading...");
        PluginManager pluginManager = this.getServer().getPluginManager();
        registerEventListeners(pluginManager);
        setDefaultGameRules();
        currentMap = new MapInformation(Bukkit.getWorld("world"));
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
        killItems();
    }

    public void removeAllBlocksPlacedByPlayers() {
        for (Block block : recordedBlocks) {
            block.setType(Material.AIR);
        }
    }

    private void resetBridge() {
        pasteSchematic("mainbridge", new Location(world, 0, 93, 0, 0, 0), true);
    }

    @SuppressWarnings("deprecation")
    private void pasteSchematic(String schematicName, Location loc, boolean noAir) {
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);
        File file = new File(getDataFolder() + File.separator + "schematics" + File.separator + schematicName + ".schematic");
        if (!file.exists()) {
            System.out.println("Could not find file");
            return;
        }
        try {
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
            System.out.println("format: " + MCEditSchematicFormat.getFormat(file).toString());
            clipboard.paste(editSession, new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), noAir);
        } catch (DataException | IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

    private void killItems() {
        for (Iterator<Entity> entityIterator = world.getEntities().iterator(); entityIterator.hasNext();) {
            Entity entity = entityIterator.next();
            if (entity instanceof Item) {
                entity.remove();
            }
        }
    }

}
