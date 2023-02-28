package practice.hippo.events.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import practice.hippo.logic.HippoPractice;
import practice.hippo.logic.MapLogic;

public class BlockDamageHandler implements Listener {

    private final HippoPractice parentPlugin;

    public BlockDamageHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        MapLogic mapLogic = parentPlugin.getMapLogic(event.getPlayer());
        if (mapLogic.awaitingLeftClick) {
            parentPlugin.revertGlassToClay(mapLogic);
        }
    }
}
