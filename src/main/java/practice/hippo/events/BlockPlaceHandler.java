package practice.hippo.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import practice.hippo.logic.MapInformation;
import practice.hippo.logic.PluginMain;
import practice.hippo.util.BoundingBox;

public class BlockPlaceHandler implements Listener {

    private final PluginMain parentPlugin;

    public BlockPlaceHandler(PluginMain parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (isBlockWithinLimits(block)) {
                // the server will remember which blocks were placed by players
                block.setMetadata("placed by player", new FixedMetadataValue(parentPlugin, ""));
                parentPlugin.recordedBlocks.add(block);
            } else {
                player.sendMessage(ChatColor.RED + "You can't place blocks there!");
                event.setCancelled(true);
            }
        }
    }

    private boolean isBlockWithinLimits(Block block) {
        MapInformation currentMap = parentPlugin.getCurrentMap();
        Location location = block.getLocation();
        BoundingBox buildLimits = currentMap.getBuildLimits();
        return buildLimits.containsInclusive(location.toVector());
    }

}