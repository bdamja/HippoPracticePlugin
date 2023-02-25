package practice.hippo.events.block;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import practice.hippo.logic.MapLogic;
import practice.hippo.logic.HippoPractice;
import practice.hippo.util.BoundingBox;

public class BlockBreakHandler implements Listener {

    private final HippoPractice parentPlugin;

    public BlockBreakHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        MapLogic mapLogic = parentPlugin.playerMap.get(player.getUniqueId());
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (!isBlockPartOfTheBridge(block, mapLogic) && !wasBlockPlacedByPlayer(block, player.getName())) {
                player.sendMessage(ChatColor.RED + "You can't break that block!");
                event.setCancelled(true);
            } else if (wasBlockPlacedByPlayer(block, player.getName())) {
                mapLogic.getRecordedBlocks().remove(block);
            }
        }
    }

    private boolean isBlockPartOfTheBridge(Block block, MapLogic mapLogic) {
        Location location = block.getLocation();
        BoundingBox bridgeDimensions = mapLogic.getBridgeDimensions();
        return bridgeDimensions.containsInclusive(location.toVector());
    }

    private boolean wasBlockPlacedByPlayer(Block block, String playerName) {
        return block.hasMetadata("placed by " + playerName);
    }

}
