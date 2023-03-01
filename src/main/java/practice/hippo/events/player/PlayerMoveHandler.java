package practice.hippo.events.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import practice.hippo.logic.HippoPractice;
import practice.hippo.logic.HippoPlayer;

import java.io.IOException;

public class PlayerMoveHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerMoveHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws IOException {
        Player player = event.getPlayer();
        HippoPlayer hippoPlayer = parentPlugin.getMapLogic(player);
        if (hippoPlayer != null) {
            if (hippoPlayer.awaitingMove) {
                hippoPlayer.getTimer().setStartTime();
                hippoPlayer.awaitingMove = false;
            }
        }
        if (player.getLocation().getY() < HippoPractice.VOID_LEVEL && player.getGameMode() != GameMode.CREATIVE) {
            parentPlugin.resetPlayerAndSendToSpawn(player);
        }
    }

}
