package practice.hippo.events.player;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import practice.hippo.HippoPractice;
import practice.hippo.logic.ItemGUIManager;

import java.io.IOException;

public class PlayerInteractListener implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerInteractListener(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        if (!event.getPlayer().getWorld().getName().equals(HippoPractice.INSTANCE.worldName)) return;
        if (parentPlugin.getHippoPlayer(event.getPlayer()).isEditingKit) return;
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack heldItem = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        if (heldItem != null) {
            if (heldItem.getType() == HippoPractice.RESET_MATERIAL) {
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    parentPlugin.resetPlayerAndSendToSpawn(player);
                    parentPlugin.resetMap(player);
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.25f, 1.0f);
                }
            } else if (heldItem.getType() == HippoPractice.SETTINGS_MATERIAL) {
                player.openInventory(ItemGUIManager.createSettingsGUI(player));
            }
        }
    }

}
