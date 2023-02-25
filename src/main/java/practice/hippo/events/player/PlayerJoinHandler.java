package practice.hippo.events.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import practice.hippo.logic.Inventory;
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
        parentPlugin.getSchematicPaster().loadMap("viewbox");
        Player player = event.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        Inventory.hardInventoryClear(player);
        parentPlugin.teleportToCenterLocation(player);
    }

}
