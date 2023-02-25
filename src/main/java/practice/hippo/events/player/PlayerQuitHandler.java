package practice.hippo.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import practice.hippo.logic.HippoPractice;

import java.util.UUID;

public class PlayerQuitHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerQuitHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        parentPlugin.playerMap.remove(uuid);
    }

}
