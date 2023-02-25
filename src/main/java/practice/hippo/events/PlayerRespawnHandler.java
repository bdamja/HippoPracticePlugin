package practice.hippo.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import practice.hippo.logic.PluginMain;

import java.io.IOException;

public class PlayerRespawnHandler implements Listener {

    private final PluginMain parentPlugin;

    public PlayerRespawnHandler(PluginMain parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) throws IOException {
        parentPlugin.refreshPlayerAttributes(event.getPlayer());
    }

}