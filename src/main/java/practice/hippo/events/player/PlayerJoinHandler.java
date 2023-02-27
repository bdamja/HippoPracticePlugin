package practice.hippo.events.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import practice.hippo.logic.InventoryLogic;
import practice.hippo.logic.HippoPractice;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import practice.hippo.logic.MapLogic;

import java.io.IOException;

public class PlayerJoinHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerJoinHandler(HippoPractice parentPlugin) {
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
        InventoryLogic.hardInventoryClear(player);
        parentPlugin.playerMap.put(player.getUniqueId(), new MapLogic(parentPlugin.world, "no_map", player.getUniqueId(), parentPlugin));
        parentPlugin.teleportToCenterLocation(player);
        parentPlugin.scoreboardLogic.makeBoard(player);
    }

}
