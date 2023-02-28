package practice.hippo.events.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import practice.hippo.logic.*;

import java.io.IOException;

public class PlayerJoinHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerJoinHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        InventoryLogic.hardInventoryClear(player);
        Plot plot = new Plot(parentPlugin);
        parentPlugin.playerMap.put(player.getUniqueId(), new MapLogic(plot, parentPlugin.world, "no_map", player, parentPlugin));
        parentPlugin.getSchematicPaster().loadViewBox(plot);
        parentPlugin.teleportToCenterLocation(player);
        parentPlugin.getMapLogic(player).getScoreboardLogic().makeBoard(player);
        ChatLogic.sendWelcomeMessage(player);
    }

}
