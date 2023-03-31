package practice.hippo.events.block;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import practice.hippo.logic.HippoPlayer;
import practice.hippo.HippoPractice;
import practice.hippo.util.BoundingBox;

import java.io.IOException;

public class BlockBreakHandler implements Listener {

    private final HippoPractice parentPlugin;

    public BlockBreakHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws IOException {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        HippoPlayer hippoPlayer = parentPlugin.getHippoPlayer(player);
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (!isBlockPartOfTheBridge(block, hippoPlayer) && !wasBlockPlacedByPlayer(block, player.getName())) {
                player.sendMessage(ChatColor.RED + "You can't break that block!");
                event.setCancelled(true);
            } else if (wasBlockPlacedByPlayer(block, player.getName())) {
                hippoPlayer.getRecordedBlocks().remove(block);
                if (hippoPlayer.isBlockLocationPartOfHippo(block.getLocation())) {
                    hippoPlayer.getHippoBlocks().add(block.getLocation());
                }
            }
        }
    }

    private boolean isBlockPartOfTheBridge(Block block, HippoPlayer hippoPlayer) {
        Location location = block.getLocation();
        BoundingBox bridgeDimensions = hippoPlayer.getBridgeDimensions();
        return bridgeDimensions.containsInclusive(location.toVector());
    }

    private boolean wasBlockPlacedByPlayer(Block block, String playerName) {
        return block.hasMetadata("placed by " + playerName);
    }

}
