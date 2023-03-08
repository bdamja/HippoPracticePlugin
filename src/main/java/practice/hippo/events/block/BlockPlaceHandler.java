package practice.hippo.events.block;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import practice.hippo.logic.HippoPractice;
import practice.hippo.logic.HippoPlayer;
import practice.hippo.util.BoundingBox;

import java.util.ArrayList;

public class BlockPlaceHandler implements Listener {

    private final HippoPractice parentPlugin;

    public BlockPlaceHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        HippoPlayer hippoPlayer = parentPlugin.getHippoPlayer(player);
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (isBlockWithinLimits(block, hippoPlayer) && !isBlockInBlacklistedRegion(block, hippoPlayer)) {
                // the server will remember which blocks were placed by players
                block.setMetadata("placed by " + player.getName(), new FixedMetadataValue(parentPlugin, ""));
                hippoPlayer.getRecordedBlocks().add(block);
                if (!hippoPlayer.hasFinishedHippo) {
                    hippoPlayer.hasFinishedHippo = checkCompleteStructure(block, hippoPlayer);
                    if (hippoPlayer.hasFinishedHippo) {
                        parentPlugin.completeHippo(hippoPlayer, player);
                    }
                }
                if (hippoPlayer.awaitingLeftClick) {
                    parentPlugin.revertGlassToClay(hippoPlayer);
                }
            } else {
                player.sendMessage(ChatColor.RED + "You can't place blocks there!");
                event.setCancelled(true);
            }
        }
    }

    private boolean isBlockWithinLimits(Block block, HippoPlayer hippoPlayer) {
        Location location = block.getLocation();
        BoundingBox buildLimits = hippoPlayer.getBuildLimits();
        return buildLimits.containsInclusive(location.toVector());
    }

    private boolean checkCompleteStructure(Block block, HippoPlayer hippoPlayer) {
        if (!hippoPlayer.getHippoBlocks().isEmpty()) {
            hippoPlayer.getHippoBlocks().remove(block.getLocation());
            return hippoPlayer.getHippoBlocks().isEmpty();
        }
        return false;
    }

    private boolean isBlockInBlacklistedRegion(Block block, HippoPlayer hippoPlayer) {
        ArrayList<BoundingBox> blacklistedRegions = hippoPlayer.getBlacklistedRegions();
        for (BoundingBox region : blacklistedRegions) {
            if (region.containsInclusive(block.getLocation().toVector())) {
                return true;
            }
        }
        return false;
    }

}