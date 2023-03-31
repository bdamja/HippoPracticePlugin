package practice.hippo.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import practice.hippo.HippoPractice;

import java.io.IOException;

public class PlayerRespawnHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerRespawnHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) throws IOException {
        parentPlugin.resetPlayerAndSendToSpawn(event.getPlayer());
    }

}