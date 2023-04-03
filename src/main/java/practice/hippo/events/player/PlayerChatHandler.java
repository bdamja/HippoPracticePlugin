package practice.hippo.events.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import practice.hippo.HippoPractice;

public class PlayerChatHandler implements Listener {

    public PlayerChatHandler() { }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().getWorld().getName().equals(HippoPractice.INSTANCE.worldName)) return;
        String msg = event.getMessage();
        event.setCancelled(true);
        Bukkit.broadcastMessage("" + ChatColor.GREEN + event.getPlayer().getName() + ChatColor.WHITE + ": " + msg);
    }

}
