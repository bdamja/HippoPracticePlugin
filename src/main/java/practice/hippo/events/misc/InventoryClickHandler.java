package practice.hippo.events.misc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import practice.hippo.logic.HippoPlayer;
import practice.hippo.HippoPractice;
import practice.hippo.logic.ItemGUIManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InventoryClickHandler implements Listener {

    private final HippoPractice parentPlugin;

    public InventoryClickHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {
        if (!event.getWhoClicked().getWorld().getName().equals(parentPlugin.worldName)) return;
        Player player = (Player) event.getWhoClicked();
        HippoPlayer hippoPlayer = parentPlugin.getHippoPlayer(player);
        if (hippoPlayer.isEditingKit) {
            if (event.isRightClick() || event.isShiftClick()) {
                event.setCancelled(true);
            }
        } else if (isInSettingsGUI(event.getClickedInventory())) {
            ItemGUIManager.performItemClickAction(player, event.getCurrentItem());
            event.setCancelled(true);
        }
    }

    public static boolean isInSettingsGUI(Inventory inventory) {
        return inventory.getName().equals("Hippo Practice");
    }
}
