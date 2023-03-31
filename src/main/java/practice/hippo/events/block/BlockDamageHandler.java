package practice.hippo.events.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import practice.hippo.logic.HippoPlayer;
import practice.hippo.HippoPractice;

public class BlockDamageHandler implements Listener {

    private final HippoPractice parentPlugin;

    public BlockDamageHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        HippoPlayer hippoPlayer = parentPlugin.getHippoPlayer(event.getPlayer());
        if (hippoPlayer.awaitingLeftClick) {
            parentPlugin.revertGlassToClay(hippoPlayer);
        }
    }
}
