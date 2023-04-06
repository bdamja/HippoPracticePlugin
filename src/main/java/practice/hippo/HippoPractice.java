package practice.hippo;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import practice.hippo.commands.HippoPracticeCommand;
import practice.hippo.events.block.BlockBreakHandler;
import practice.hippo.events.block.BlockDamageHandler;
import practice.hippo.events.block.BlockPlaceHandler;
import practice.hippo.events.entity.EntityDamageHandler;
import practice.hippo.events.misc.InventoryClickHandler;
import practice.hippo.events.misc.InventoryDragHandler;
import practice.hippo.events.misc.WeatherChangeHandler;
import practice.hippo.events.player.*;
import practice.hippo.hippodata.HippoData;
import practice.hippo.logic.*;
import practice.hippo.logic.Timer;
import practice.hippo.mapdata.MapData;
import practice.hippo.playerdata.PlayerData;
import practice.hippo.playerdata.PlayerDataFormat;
import practice.hippo.util.MongoDB;
import practice.hippo.util.Offset;
import practice.hippo.util.Test;
import practice.hippo.util.UUIDFetcher;

import java.io.*;
import java.util.*;

import static practice.hippo.util.Side.blue;
import static practice.hippo.util.Side.red;

public class HippoPractice extends JavaPlugin implements Listener {

    public static HippoPractice INSTANCE;

    public static final int VOID_LEVEL = 83;
    public static final int NUM_PARTICLES = 350;
    public static final int DISTANCE_BETWEEN_PLOTS = 41;
    public static final boolean USE_DATABASE = true;
    public static final Material RESET_MATERIAL = Material.REDSTONE;

    public String worldName;
    public String ip;
    public long commandCooldownMilliseconds;

    public static SchematicLogic schematicPaster = null;
    public World world;
    public HolographicDisplaysAPI holographicDisplaysAPI;
    public static TreeMap<String, String> maps = new TreeMap<>();
    public static ArrayList<String> kitActions = new ArrayList<String>(){ { add("edit"); add("save"); } };
    private static final ArrayList<Plot> plots = new ArrayList<>();
    public ScoreboardLogic scoreboardLogic = null;
    public HashMap<UUID, HippoPlayer> playerMap = new HashMap<>();
    public HashMap<Player, Long> lastExecutedHeftyCommand = new HashMap<>();

    @Override
    public void onEnable() {
        INSTANCE = this;
        readConfigValues();
        PluginManager pluginManager = this.getServer().getPluginManager();
        registerEventListeners(pluginManager);
        setDefaultGameRules();
        schematicPaster = new SchematicLogic(this, Bukkit.getWorld(worldName));
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new HippoPracticeCommand(this));
        try {
            addMapsToQueue();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        manager.getCommandCompletions().registerCompletion("mapNames", c -> maps.descendingKeySet());
        manager.getCommandCompletions().registerCompletion("kitActions", c -> kitActions);
        scoreboardLogic = new ScoreboardLogic(this);
        setPlotList();
        if (USE_DATABASE) {
            MongoDB.init();
        }
        holographicDisplaysAPI = HolographicDisplaysAPI.get(this);
    }

    @Override
    public void onDisable() {
        if (USE_DATABASE) {
            MongoDB.close();
        }
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
        pluginManager.registerEvents(new PlayerChatHandler(), this);
        pluginManager.registerEvents(new WeatherChangeHandler(), this);
        pluginManager.registerEvents(new InventoryClickHandler(this), this);
        pluginManager.registerEvents(new InventoryDragHandler(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
    }

    private void setDefaultGameRules() {
        world = Bukkit.getWorld(worldName);
        if (world != null) {
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("naturalRegeneration", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("randomTickSpeed", "0");
        } else {
            getLogger().info("The world name in the config could not be found, so Hippo Practice won't be functional in this server.");
        }
    }

    private void readConfigValues() {
        worldName = getConfig().getString("world_name");
        ip = getConfig().getString("ip");
        commandCooldownMilliseconds = getConfig().getLong("command_cooldown_ms");
        this.saveConfig();
    }

    public File getPluginsDirSubdir(String subdir) {
        return new File(getDataFolder() + File.separator + subdir);
    }

    private void addMapsToQueue() throws FileNotFoundException {
        File mapDataDirectory = getPluginsDirSubdir("mapdata");
        if (!mapDataDirectory.exists()) {
            getLogger().severe("Error when trying to load mapdata directory: Could not find directory: " + mapDataDirectory);
        } else {
            for (File mapDataFile : mapDataDirectory.listFiles()) {
                Gson gson = new Gson();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(mapDataFile));
                MapData mapData = gson.fromJson(bufferedReader, MapData.class);
                maps.put(mapData.getMapName(), mapData.getMapText());
            }
        }
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
        getHippoPlayer(player).isEditingKit = false;
        InventoryLogic.loadInventory(player, getHippoPlayer(player).getPlot().getSide(), getHippoPlayer(player).getPlayerData());
    }

    public SchematicLogic getSchematicPaster() {
        return schematicPaster;
    }

    public void resetMap(Player player) throws IOException {
        HippoPlayer hippoPlayer = getHippoPlayer(player);
        Plot plot = hippoPlayer.getPlot();
        String mapName = hippoPlayer.getMapName();
        removeAllBlocksPlacedByPlayer(player);
        killItems();
        schematicPaster.loadMainBridge(hippoPlayer);
        HippoPlayer.cancelTasksIfPresent(hippoPlayer);
        hippoPlayer = new HippoPlayer(plot, world, mapName, player, this);
        playerMap.replace(player.getUniqueId(), hippoPlayer);
        hippoPlayer.resetVisualTimer();
    }

    public void removeAllBlocksPlacedByPlayer(Player player) {
        HippoPlayer hippoPlayer = getHippoPlayer(player);
        if (hippoPlayer != null) {
            Queue<Block> recordedBlocks = hippoPlayer.getRecordedBlocks();
            for (Block block : recordedBlocks) {
                block.setType(Material.AIR);
            }
            recordedBlocks.clear();
        }
    }

    public Queue<Block> getAllBlocksPlacedByPlayer(Player player) {
        HippoPlayer hippoPlayer = getHippoPlayer(player);
        return hippoPlayer.getRecordedBlocks();
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
            HippoPlayer.cancelTasksIfPresent(getHippoPlayer(player));
            HippoPlayer hippoPlayer = new HippoPlayer(getHippoPlayer(player).getPlot(), world, mapName, player, this);
            playerMap.replace(player.getUniqueId(), hippoPlayer);
            schematicPaster.loadMap(hippoPlayer);
            resetMap(player);
            resetPlayerAndSendToSpawn(player);
            scoreboardLogic.updateMapName(player, hippoPlayer.mapText());
            scoreboardLogic.updatePB(hippoPlayer.getPlayerData(), hippoPlayer.getMapName());
            reloadChunks(player);
            hippoPlayer.updateLeaderboardsFully();
        }
    }

    public void teleportToSpawnLocation(Player player) {
        HippoPlayer hippoPlayer = getHippoPlayer(player);
        player.teleport(hippoPlayer.getSpawnPoint());
    }

    public void teleportToViewLocation(Player player) {
        HippoPlayer hippoPlayer = getHippoPlayer(player);
        player.teleport(Offset.location(hippoPlayer.getPlot(), hippoPlayer.getViewLocation(), false));
    }

    public void teleportToCenterLocation(Player player) {
        HippoPlayer hippoPlayer = getHippoPlayer(player);
        player.teleport(Offset.location(hippoPlayer.getPlot(), hippoPlayer.getMapCenter(), false));
    }

    public void completeHippo(HippoPlayer hippoPlayer, Player player) {
        long ms = hippoPlayer.getTimer().computeTime();
        String finalTime = practice.hippo.logic.Timer.computeTimeFormatted(ms);
        ChatLogic.sendHippoCompletion(hippoPlayer, ms, player);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 0.8f);
        summonCompletionParticles(player);
        hippoPlayer.hasFinishedHippo = true;
        hippoPlayer.stopVisualTimer();
        hippoPlayer.updateVisualTimer(player.getScoreboard(), finalTime);
        checkNewPB(hippoPlayer.getPlayerData(), hippoPlayer.getMapData().getMapName(), ms);
    }

    private void summonCompletionParticles(Player player) {
        double x = player.getLocation().getX();
        double y = player.getLocation().getY() + 1;
        double z = player.getLocation().getZ();
        PacketPlayOutWorldParticles particles;
        Plot plot = getHippoPlayer(player).getPlot();
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

    public void checkNewPB(PlayerData playerData, String mapName, long time) {
        long currentPB = playerData.getPB(mapName);
        if (currentPB == -1 || time < currentPB) {
            playerData.setPB(mapName, time);
            scoreboardLogic.updatePB(playerData, mapName);
        }
    }

    public final void setPlotList() {
        int maxPlayers = Bukkit.getMaxPlayers();
        int z = 0;
        for (int i = 0; i < maxPlayers/2; i++) {
            plots.add(new Plot(this, new Location(world, 0, 0, z), red));
            plots.add(new Plot(this, new Location(world, 0, 0, z), blue));
            z += DISTANCE_BETWEEN_PLOTS;
        }
    }

    public ArrayList<Plot> getPlotList() {
        return plots;
    }

    public HippoPlayer getHippoPlayer(Player player) {
        return playerMap.get(player.getUniqueId());
    }

    public boolean isPlotOccupied(Plot plot) {
        boolean isOccupied = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            HippoPlayer hippoPlayer = getHippoPlayer(player);
            if (hippoPlayer != null) {
                Plot occupiedPlot = hippoPlayer.getPlot();
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
    public void showMissingBlocks(HippoPlayer hippoPlayer) {
        for (Block block : hippoPlayer.getRecordedBlocks()) {
            block.setType(Material.STAINED_GLASS);
            block.setData(hippoPlayer.getColorData());
        }
        hippoPlayer.awaitingLeftClick = true;
        showMissingParticles(hippoPlayer);
    }

    @SuppressWarnings("deprecation")
    public void revertGlassToClay(HippoPlayer hippoPlayer) {
        for (Block block : hippoPlayer.getRecordedBlocks()) {
            block.setType(Material.STAINED_CLAY);
            block.setData(hippoPlayer.getColorData());
        }
        hippoPlayer.awaitingLeftClick = false;
        hippoPlayer.stopParticleSummoning();
    }

    private void showMissingParticles(HippoPlayer hippoPlayer) {
        ArrayList<Location> missingParticleLocations = getMissingBlockLocations(hippoPlayer);
        hippoPlayer.startParticleSummoning(missingParticleLocations, 25);
    }

    private ArrayList<Location> getMissingBlockLocations(HippoPlayer hippoPlayer) {
        ArrayList<Location> missingBlockLocations = new ArrayList<>();
        for (Location location : hippoPlayer.getHippoBlocks()) {
            if (world.getBlockAt(location).getType().equals(Material.AIR)) {
                double centerX = location.getX() + 0.5;
                double centerY = location.getY() + 0.5;
                double centerZ = location.getZ() + 0.5;
                missingBlockLocations.add(new Location(world, centerX, centerY, centerZ));
            }
        }
        return missingBlockLocations;
    }

    private void reloadChunks(Player player) {
        player.teleport(new Location(world, player.getLocation().getX(), VOID_LEVEL + 3, player.getLocation().getZ() + 1000));
    }

    public String getPlayerInfo(String playerName) throws Exception {
        String uuid = getUUIDStrFromPlayerName(playerName);
        if (!uuid.isEmpty()) {
            if (USE_DATABASE) {
                String info = getPlayerInfoFromPlayerDataFile(playerName, uuid);
                if (info != null) {
                    return info;
                } else {
                    return ChatColor.RED + "Hippo Practice could not find information on the player \"" + playerName + "\"";
                }
            } else {
                File playerDataFile = new File(getPluginsDirSubdir("playerdata") + File.separator + uuid + ".json");
                if (playerDataFile.exists()) {
                    return getPlayerInfoFromPlayerDataFile(playerName, uuid);
                } else {
                    return ChatColor.RED + "Hippo Practice could not find information on the player \"" + playerName + "\"";
                }
            }
        }
        return ChatColor.RED + "Mojang could not find information on the player \"" + playerName + "\"";
    }

    public static String getUUIDStrFromPlayerName(String playerName) throws Exception {
        ArrayList<String> nameList = new ArrayList<>();
        nameList.add(playerName);
        UUIDFetcher fetcher = new UUIDFetcher(nameList);
        Map<String, UUID> uuidMap = fetcher.call();
        if (uuidMap.size() > 0) {
            return uuidMap.entrySet().iterator().next().getValue().toString();
        }
        return "";
    }

    public String getPlayerInfoFromPlayerDataFile(String playerName, String uuid) throws FileNotFoundException {
        String msg = ChatColor.GRAY + "Showing info for the player " + ChatColor.GREEN + playerName + ChatColor.GRAY + ":";
        PlayerData playerData = new PlayerData(this, playerName, uuid);
        if (playerData.getData() != null) {
            boolean completeTimesheet = true;
            msg = msg.concat("\n" + ChatLogic.PREFIX + ChatColor.GRAY + "List of all personal best times:");
            for (Map.Entry<String, String> mapElement : maps.entrySet()) {
                String mapNameFormatted = mapElement.getValue();
                String mapName = mapElement.getKey();
                long ms = playerData.getPB(mapName);
                String personalBest = practice.hippo.logic.Timer.computeTimeFormatted(ms);
                if (!personalBest.equals("0.000")) {
                    msg = msg.concat("\n" + ChatColor.GRAY + "  » " + mapNameFormatted + ChatColor.GRAY + ": " + ChatColor.AQUA + personalBest);
                } else {
                    if (doesHippoExist(mapName)) {
                        completeTimesheet = false;
                    }
                }
            }
            String totalTime = ChatColor.YELLOW + Timer.computeTimeFormatted(playerData.getTotalTime());
            msg = msg.concat("\n" + ChatColor.GRAY + "  » " + ChatColor.YELLOW + ChatColor.BOLD + "Total" + ChatColor.GRAY + ": " + totalTime);
            if (!completeTimesheet) {
                msg = msg.concat(ChatColor.GRAY + " (Incomplete)");
            }
            return msg;
        } else {
            return null;
        }
    }

    public boolean doesHippoExist(String mapName) {
        if (USE_DATABASE) {
            return MongoDB.doesHippoExist(mapName);
        } else {
            return new File(getPluginsDirSubdir("hippodata") + File.separator + mapName + ".json").exists();
        }
    }

    public void beginEditingKit(Player player) {
        HippoPlayer hippoPlayer = getHippoPlayer(player);
        hippoPlayer.isEditingKit = true;
        player.setGameMode(GameMode.ADVENTURE);
        InventoryLogic.loadInventory(player, hippoPlayer.getPlot().getSide(), hippoPlayer.getPlayerData());
    }

    public void saveKit(Player player) {
        HippoPlayer hippoPlayer = getHippoPlayer(player);
        if (hippoPlayer.isEditingKit) {
            hippoPlayer.isEditingKit = false;
            player.setGameMode(GameMode.SURVIVAL);
            writeCurrentLayoutToFile(hippoPlayer);
        }
    }

    private void writeCurrentLayoutToFile(HippoPlayer hippoPlayer) {
        Player player = hippoPlayer.getPlayer();
        Iterator<ItemStack> inventoryIterator = player.getInventory().iterator();
        PlayerData playerData = hippoPlayer.getPlayerData();
        int pickSlot = InventoryLogic.DEFAULT_PICK_SLOT;
        int blocks1Slot = InventoryLogic.DEFAULT_BLOCKS1_SLOT;
        int blocks2Slot = InventoryLogic.DEFAULT_BLOCKS2_SLOT;
        int resetItemSlot = InventoryLogic.DEFAULT_RESET_ITEM_SLOT;
        int index = 0;
        int blocksCount = 0;
        while (inventoryIterator.hasNext()) {
            ItemStack itemStack = inventoryIterator.next();
            if (itemStack != null) {
                if (itemStack.getType().equals(Material.DIAMOND_PICKAXE)) {
                    pickSlot = index;
                }
                if (itemStack.getType().equals(Material.STAINED_CLAY)) {
                    if (blocksCount == 0) {
                        blocks1Slot = index;
                        blocksCount++;
                    } else {
                        blocks2Slot = index;
                    }
                }
                if (itemStack.getType().equals(HippoPractice.RESET_MATERIAL)) {
                    resetItemSlot = index;
                }
            }
            index++;
        }
        playerData.setPickSlot(pickSlot);
        playerData.setBlocks1Slot(blocks1Slot);
        playerData.setBlocks2Slot(blocks2Slot);
        playerData.setResetItemSlot(resetItemSlot);
        uploadPlayerData(hippoPlayer.getPlayerData());
    }

    public static void uploadHippoData(HippoData hippoData) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        if (USE_DATABASE) {
            MongoDB.upsertHippoData(hippoData.getMapName(), gson.toJson(hippoData));
        } else {
            File file = new File("./plugins/HippoPractice/hippodata/" + hippoData.getMapName() + ".json");
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(hippoData, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void uploadPlayerData(PlayerData playerData) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        PlayerDataFormat data = playerData.getData();
        if (USE_DATABASE) {
            MongoDB.upsertPlayerData(playerData.getPlayerUUID(), gson.toJson(data));
        } else {
            File file = new File("./plugins/HippoPractice/playerdata/" + playerData.getPlayerUUID() + ".json");
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(data, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
