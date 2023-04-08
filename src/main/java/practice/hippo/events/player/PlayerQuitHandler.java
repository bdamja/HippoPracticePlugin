package practice.hippo.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import practice.hippo.HippoPractice;

import java.util.UUID;

public class PlayerQuitHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerQuitHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().getWorld().getName().equals(HippoPractice.INSTANCE.worldName)) return;
        event.setQuitMessage("§b" + event.getPlayer().getName() + " §7has left.");
        handleQuit(event.getPlayer(), parentPlugin);
    }

    public static void handleQuit(Player player, HippoPractice parentPlugin) {
        UUID uuid = player.getUniqueId();
        parentPlugin.getHippoPlayer(player).deleteLeaderboards();
        parentPlugin.playerMap.remove(uuid);
    }

}
