package practice.hippo.events.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import practice.hippo.HippoPractice;
import practice.hippo.logic.HippoPlayer;
import practice.hippo.util.BoundingBox;

import java.io.IOException;

public class PlayerMoveHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerMoveHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws IOException {
        Player player = event.getPlayer();
        HippoPlayer hippoPlayer = parentPlugin.getHippoPlayer(player);
        if (hippoPlayer != null) {
            if (hippoPlayer.awaitingMove) {
                hippoPlayer.getTimer().setStartTime();
                hippoPlayer.awaitingMove = false;
            }
            if (!hippoPlayer.getMapName().equals("null")) {
                BoundingBox buildLimits = hippoPlayer.getBuildLimits();
                double x = Math.abs(player.getLocation().getX());
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                boolean isInCreative = player.getGameMode() == GameMode.CREATIVE;
                double minX = Math.min(Math.abs(buildLimits.getMinX()), Math.abs(buildLimits.getMaxX()));
                if (!isInCreative && (x < minX || z < buildLimits.getMinZ() || z > buildLimits.getMaxZ())) {
                    parentPlugin.resetMap(player);
                    parentPlugin.resetPlayerAndSendToSpawn(player);
                } else if (!isInCreative && y < HippoPractice.VOID_LEVEL) {
                    parentPlugin.resetPlayerAndSendToSpawn(player);
                }
            }
        }
    }

}
