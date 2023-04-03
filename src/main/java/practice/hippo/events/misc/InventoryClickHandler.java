package practice.hippo.events.misc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import practice.hippo.logic.HippoPlayer;
import practice.hippo.HippoPractice;

public class InventoryClickHandler implements Listener {

    private final HippoPractice parentPlugin;

    public InventoryClickHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getWhoClicked().getWorld().getName().equals(parentPlugin.worldName)) return;
        Player player = (Player) event.getWhoClicked();
        HippoPlayer hippoPlayer = parentPlugin.getHippoPlayer(player);
        if (hippoPlayer.isEditingKit) {
            if (event.isRightClick() || event.isShiftClick()) {
                event.setCancelled(true);
            }
        }
    }
}
