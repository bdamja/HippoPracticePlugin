package practice.hippo.events.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import practice.hippo.logic.HippoPractice;
import practice.hippo.logic.MapLogic;

import java.io.IOException;

public class PlayerMoveHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerMoveHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws IOException {
        Player player = event.getPlayer();
        MapLogic mapLogic = parentPlugin.getMapLogic(player);
        if (mapLogic != null) {
            if (mapLogic.awaitingMove) {
                mapLogic.getTimer().setStartTime();
                mapLogic.awaitingMove = false;
            }
        }
        if (player.getLocation().getY() < HippoPractice.VOID_LEVEL && player.getGameMode() != GameMode.CREATIVE) {
            parentPlugin.resetPlayerAndSendToSpawn(player);
        }
    }

}
