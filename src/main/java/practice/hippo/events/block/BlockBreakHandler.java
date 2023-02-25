package practice.hippo.events.block;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import practice.hippo.logic.MapInformation;
import practice.hippo.logic.PluginMain;
import practice.hippo.util.BoundingBox;

public class BlockBreakHandler implements Listener {

    private final PluginMain parentPlugin;

    public BlockBreakHandler(PluginMain parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (!isBlockPartOfTheBridge(block) && !wasBlockPlacedByPlayer(block)) {
                player.sendMessage(ChatColor.RED + "You can't break that block!");
                event.setCancelled(true);
            }
        }
    }

    private boolean isBlockPartOfTheBridge(Block block) {
        MapInformation currentMap = parentPlugin.getCurrentMap();
        Location location = block.getLocation();
        BoundingBox bridgeDimensions = currentMap.getBridgeDimensions();
        return bridgeDimensions.containsInclusive(location.toVector());
    }

    private boolean wasBlockPlacedByPlayer(Block block) {
        return block.hasMetadata("placed by player");
    }

}
