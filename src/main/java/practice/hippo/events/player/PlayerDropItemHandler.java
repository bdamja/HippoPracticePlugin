package practice.hippo.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import practice.hippo.HippoPractice;

public class PlayerDropItemHandler implements Listener {

    public PlayerDropItemHandler() { }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!event.getPlayer().getWorld().getName().equals(HippoPractice.INSTANCE.worldName)) return;
        event.setCancelled(true);
    }

}