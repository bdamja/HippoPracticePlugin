package practice.hippo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import practice.hippo.logic.PluginMain;

public class PlayerMoveHandler implements Listener {

    private final PluginMain parentPlugin;

    public PlayerMoveHandler(PluginMain parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() < PluginMain.VOID_LEVEL) {
            parentPlugin.refreshPlayerAttributes(player);
        }
    }

}
