package practice.hippo.events.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import practice.hippo.logic.HippoPractice;

import java.io.IOException;

public class PlayerMoveHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerMoveHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws IOException {
        Player player = event.getPlayer();
        if (player.getLocation().getY() < HippoPractice.VOID_LEVEL && player.getGameMode() != GameMode.CREATIVE) {
            parentPlugin.resetPlayerAndSendToSpawn(player);
        }
    }

}
