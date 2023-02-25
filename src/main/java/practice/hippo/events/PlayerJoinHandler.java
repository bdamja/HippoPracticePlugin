package practice.hippo.events;

import practice.hippo.logic.PluginMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class PlayerJoinHandler implements Listener {

    private final PluginMain parentPlugin;

    public PlayerJoinHandler(PluginMain parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        event.setJoinMessage("");
        parentPlugin.refreshPlayerAttributes(event.getPlayer());
    }

}
