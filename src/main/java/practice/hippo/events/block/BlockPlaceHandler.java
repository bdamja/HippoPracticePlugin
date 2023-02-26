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
import practice.hippo.logic.ChatLogic;
import practice.hippo.logic.MapLogic;
import practice.hippo.logic.HippoPractice;
import practice.hippo.util.BoundingBox;

public class BlockPlaceHandler implements Listener {

    private final HippoPractice parentPlugin;

    public BlockPlaceHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        MapLogic mapLogic = parentPlugin.playerMap.get(player.getUniqueId());
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (isBlockWithinLimits(block, mapLogic)) {
                // the server will remember which blocks were placed by players
                block.setMetadata("placed by " + player.getName(), new FixedMetadataValue(parentPlugin, ""));
                mapLogic.getRecordedBlocks().add(block);
                if (!mapLogic.hasFinishedHippo) {
                    mapLogic.hasFinishedHippo = checkCompleteStructure(block, mapLogic);
                    if (mapLogic.hasFinishedHippo) {
                        parentPlugin.completeHippo(mapLogic, player);
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You can't place blocks there!");
                event.setCancelled(true);
            }
        }
    }

    private boolean isBlockWithinLimits(Block block, MapLogic mapLogic) {
        Location location = block.getLocation();
        BoundingBox buildLimits = mapLogic.getBuildLimits();
        return buildLimits.containsInclusive(location.toVector());
    }

    private boolean checkCompleteStructure(Block block, MapLogic mapLogic) {
        mapLogic.getHippoBlocks().remove(block.getLocation());
        return mapLogic.getHippoBlocks().isEmpty();
    }

}